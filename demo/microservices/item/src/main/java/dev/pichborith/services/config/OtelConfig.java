package dev.pichborith.services.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.contrib.baggage.processor.BaggageLogRecordProcessor;
import io.opentelemetry.contrib.baggage.processor.BaggageSpanProcessor;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Objects.requireNonNull;

@Configuration
public class OtelConfig {

  @Value("${otel.exporter.otlp.endpoint}")
  String exporterEndpoint;

  @Bean
  OpenTelemetrySdk openTelemetrySdk(
    ContextPropagators contextPropagators,
    SpanExporter spanExporter,
    LogRecordExporter logExporter) {
    requireNonNull(contextPropagators, "contextPropagators must not be null");
    requireNonNull(spanExporter, "spanExporter must not be null");

    Resource resource = buildResource();

    var tracerProvider = SdkTracerProvider.builder()
      .setResource(resource)
      .addSpanProcessor(
        BatchSpanProcessor.builder(spanExporter).build()
      )
      .addSpanProcessor(BaggageSpanProcessor.allowAllBaggageKeys())
      .build();

    var loggerProvider = SdkLoggerProvider.builder()
      .setResource(resource)
      .addLogRecordProcessor(BatchLogRecordProcessor.builder(logExporter).build())
      .addLogRecordProcessor(BaggageLogRecordProcessor.allowAllBaggageKeys())
      .build();

    return OpenTelemetrySdk.builder()
      .setTracerProvider(tracerProvider)
      .setLoggerProvider(loggerProvider)
      .setPropagators(contextPropagators)
      .build();

  }

  @Bean
  public Tracer tracer(OpenTelemetry openTelemetry) {
    requireNonNull(openTelemetry, "openTelemetrySdk is required and missing.");
    return openTelemetry.getTracer("MicroservicesDemo");
  }

  @Bean
  SpanExporter spanExporter() {
    return OtlpGrpcSpanExporter.builder()
      .setEndpoint(exporterEndpoint)
      .build();
  }

  @Bean
  LogRecordExporter logExporter() {
    return OtlpGrpcLogRecordExporter.builder()
      .setEndpoint(exporterEndpoint)
      .build();
  }

  @Bean
  ContextPropagators contextPropagators() {
    return ContextPropagators.create(
      TextMapPropagator.composite(new B3MultiHeadersB3Propagator())
    );
  }

  @Bean
  LogRecordExporter logRecordExporter() {
    return OtlpGrpcLogRecordExporter.builder()
      .setEndpoint("http://localhost:4317")
      .build();
  }

  Resource buildResource() {
    return Resource.getDefault()
      .merge(Resource.builder()
               .put("service.name", "item-service")
               .build()
      );
  }

}

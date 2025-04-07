package dev.pichborith.demo.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.contrib.baggage.processor.BaggageSpanProcessor;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static java.util.Objects.requireNonNull;

@Configuration
@RequiredArgsConstructor
public class OtelConfig {

  @Value("${otel.exporter.otlp.endpoint}") // gRPC endpoint for Jaeger
  String exporterEndpoint;

  @Bean
  public OpenTelemetry openTelemetry(SpanExporter spanExporter) {

    Resource serviceNameResource = buildResource();

    SdkTracerProvider tracerProvider = SdkTracerProvider.builder().setResource(serviceNameResource)
                                         .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                                         .addSpanProcessor(BaggageSpanProcessor.allowAllBaggageKeys())
                                         .build();

    return OpenTelemetrySdk.builder()
             .setTracerProvider(tracerProvider)
             .build();

  }

  @Bean
  public Tracer tracer(OpenTelemetry openTelemetry) {
    requireNonNull(openTelemetry, "openTelemetrySdk is required and missing.");
    return openTelemetry.getTracer("WebfluxImplementDemo");
  }

  @Bean
  public SpanExporter spanExporter() {
    return OtlpGrpcSpanExporter.builder()
             .setEndpoint(exporterEndpoint)
             .build();
  }

  public Resource buildResource() {
    return Resource.getDefault()
             .merge(
               Resource.builder()
                 .put("service.name", "webflux-implement-demo")
                 .put("service.instance.id", getHost())
                 .build()
             );
  }

  private String getHost() {
    try {
      return InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException ignored) { }

    return "UNKNOWN";
  }

}

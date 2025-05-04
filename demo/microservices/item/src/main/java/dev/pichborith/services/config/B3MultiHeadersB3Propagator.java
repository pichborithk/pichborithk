package dev.pichborith.services.config;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.baggage.BaggageBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import io.opentelemetry.extension.trace.propagation.B3Propagator;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Slf4j
public class B3MultiHeadersB3Propagator implements TextMapPropagator {

  private final TextMapPropagator delegate;

  public B3MultiHeadersB3Propagator() {
    this.delegate = B3Propagator.injectingMultiHeaders();
  }

  @Override
  public Collection<String> fields() {
    return delegate.fields();
  }

  @Override
  public <C> void inject(Context context, C carrier, TextMapSetter<C> setter) {
    delegate.inject(context, carrier, setter);
  }

  @Override
  public <C> Context extract(Context context, C carrier, TextMapGetter<C> getter) {
    String traceId = getter.get(carrier, "x-b3-traceId");
    String spanId = getter.get(carrier, "x-b3-spanId");
    String sampleId = getter.get(carrier, "x-b3-sampled");
    Context root;

    if (traceId == null || spanId == null || sampleId == null) {
      String testId = getter.get(carrier, "test-id");
      if (testId != null) {
        CarrierWrapper<C> carrierWithB3 = getCarrierWrapper(carrier, getter, testId);
        root = delegate.extract(context, carrierWithB3.getCarrier(), carrierWithB3);
      } else {
        root = delegate.extract(context, carrier, getter);
      }
    } else {
      root = delegate.extract(context, carrier, getter);
    }
    BaggageBuilder baggege = getBaggageBuilder(carrier, getter);
    return root.with(baggege.build());
  }

  private <C> CarrierWrapper<C> getCarrierWrapper(C carrier, TextMapGetter<C> getter, String testId) {
    String syntheticTraceId = "1";
    String syntheticSpanId = "2";
    String syntheticParentSpan = "3";
    return new CarrierWrapper<>(carrier, getter, Map.of(
      "x-b3-traceId", syntheticTraceId,
      "x-b3-spanId", syntheticSpanId,
      "x-b3-parentspanid", syntheticParentSpan
    ));
  }


  private <C> BaggageBuilder getBaggageBuilder(C carrier, TextMapGetter<C> getter) {
    BaggageBuilder baggage = Baggage.builder();
    return ofNullable(getter.get(carrier, "test-id"))
      .map(value -> baggage.put("test-id", value))
      .orElse(baggage);
  }

}

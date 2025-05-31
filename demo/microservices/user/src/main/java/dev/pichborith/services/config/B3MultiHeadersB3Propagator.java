package dev.pichborith.services.config;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.baggage.BaggageBuilder;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import io.opentelemetry.extension.trace.propagation.B3Propagator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static dev.pichborith.services.common.OneDataHeaders.*;

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
  public <C> void inject(@NonNull Context context, @Nullable C carrier, @NonNull TextMapSetter<C> setter) {
    delegate.inject(context, carrier, setter);
  }

  @Override
  public <C> Context extract(@NonNull Context context, C carrier, TextMapGetter<C> getter) {
    String traceId = getter.get(carrier, X_B3_TRACE_ID);
    String spanId = getter.get(carrier, X_B3_SPAN_ID);
    String sampled = getter.get(carrier, X_B3_SAMPLED);
    Context root;

    if (traceId == null || spanId == null || sampled == null) {
      String oneDataTraceId = getter.get(carrier, ONE_DATA_TRACE_ID);
      if (oneDataTraceId != null && oneDataTraceId.split(":").length == 4) {
        CarrierWrapper<C> carrierWithB3 = getCarrierWrapper(carrier, getter, oneDataTraceId);
        root = delegate.extract(context, carrierWithB3.getCarrier(), carrierWithB3);
      } else {
        root = delegate.extract(context, carrier, getter);
      }
    } else {
      root = delegate.extract(context, carrier, getter);
    }
    BaggageBuilder baggage = getBaggageBuilder(carrier, getter);
    return root.with(baggage.build());
  }

  private <C> CarrierWrapper<C> getCarrierWrapper(C carrier, TextMapGetter<C> getter, String oneDataTraceId) {
    String[] parts = oneDataTraceId.split(":");
    return new CarrierWrapper<>(carrier, getter, Map.of(
      X_B3_TRACE_ID, parts[0],
      X_B3_SPAN_ID, parts[1],
      X_B3_PARENT_SPAN_ID, parts[2],
      X_B3_SAMPLED, "1"
    ));
  }

  private <C> BaggageBuilder getBaggageBuilder(C carrier, TextMapGetter<C> getter) {
    BaggageBuilder baggage = Baggage.current().toBuilder();

    baggage.put(
      ONE_DATA_CORRELATION_ID,
      Optional.ofNullable(getter.get(carrier, ONE_DATA_CORRELATION_ID))
        .filter(StringUtils::isNotBlank)
        .orElseGet(() -> UUID.randomUUID().toString())
    );
    Optional.ofNullable(getter.get(carrier, ONE_DATA_TRACE_ID))
      .map(value -> baggage.put(ONE_DATA_TRACE_ID, value));
    baggage.put("test", "test value");

    return baggage;
  }

}

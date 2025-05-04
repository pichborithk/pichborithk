package dev.pichborith.services.config;

import io.opentelemetry.context.propagation.TextMapGetter;
import lombok.Getter;

import java.util.Map;

public class CarrierWrapper<C> implements TextMapGetter<C> {

  @Getter
  private final C carrier;
  private final TextMapGetter<C> delegate;
  private final Map<String, String> overrideMap;

  public CarrierWrapper(C carrier, TextMapGetter<C> delegate, Map<String, String> overrideMap) {
    this.carrier = carrier;
    this.delegate = delegate;
    this.overrideMap = overrideMap;
  }

  @Override
  public Iterable<String> keys(C carrier) {
    return delegate.keys(carrier);
  }

  @Override
  public String get(C carrier, String key) {
    String overridden = overrideMap.get(key);
    if (overridden != null) {
      return overridden;
    }
    return delegate.get(carrier, key);
  }

}

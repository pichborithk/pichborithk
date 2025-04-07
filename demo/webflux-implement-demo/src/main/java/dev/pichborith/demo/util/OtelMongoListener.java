package dev.pichborith.demo.util;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class OtelMongoListener implements CommandListener {

  private final Tracer tracer;

  private final ConcurrentHashMap<Integer, Span> spanMap = new ConcurrentHashMap<>();

  @Override
  public void commandStarted(CommandStartedEvent event) {
    Span span = tracer.spanBuilder("mongo." + event.getCommandName())
                  .setAttribute("db.system", "mongodb")
                  .setAttribute("db.operation", event.getCommandName())
                  .setAttribute("db.mongodb.collection", event.getDatabaseName())
                  .startSpan();

    spanMap.put(event.getRequestId(), span);
    span.makeCurrent(); // attach context
  }

  @Override
  public void commandSucceeded(CommandSucceededEvent event) {
    Span span = spanMap.remove(event.getRequestId());
    if (span != null) {
      span.end();
    }
  }

  @Override
  public void commandFailed(CommandFailedEvent event) {
    Span span = spanMap.remove(event.getRequestId());
    if (span != null) {
      span.recordException(event.getThrowable());
      span.end();
    }
  }

}

package dev.pichborith.services.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!test")
public class TracingFilter implements WebFilter {

//  private final Tracer tracer;

  @Override
  @NonNull
  public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    long startTime = System.nanoTime();
    ServerHttpRequest request = exchange.getRequest();
//    HttpHeaders headers = request.getHeaders();
    String path = request.getURI().getPath();
    String method = request.getMethod().name();

//    String correlationId = Optional.ofNullable(headers.getFirst(ONE_DATA_CORRELATION_ID))
//      .filter(StringUtils::isNotBlank)
//      .orElse(UUID.randomUUID().toString());
//    System.out.println("Filter");

//    Span span = tracer.spanBuilder(method + " " + path)
//      .setSpanKind(SpanKind.SERVER)
//      .setParent(Context.current())
//      .setAttribute("http.method", method)
//      .setAttribute("http.path", path)
//      .setAttribute(ONE_DATA_CORRELATION_ID, correlationId)
//      .startSpan();

    return chain.filter(exchange)
      .doOnSuccess(done -> {
//        span.setStatus(StatusCode.OK);
        log.info("{} Request API Address: {}, Response Time: {}ms, HttpStatus: {}",
                 method,
                 path,
                 TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime),
                 exchange.getResponse().getStatusCode());
      })
      .doOnError(error -> {
//        span.setStatus(StatusCode.ERROR, error.getMessage());
//        span.recordException(error);
        log.error("Exception in API {}: {}", path, error.getMessage());
      });
//      .contextWrite(ctx -> ctx.put(ONE_DATA_CORRELATION_ID, correlationId))
//      .doFinally(signal -> span.end());
  }
}

package dev.pichborith.services.config;

import dev.pichborith.services.integration.ItemClient;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static dev.pichborith.services.common.OneDataHeaders.ONE_DATA_CORRELATION_ID;
import static dev.pichborith.services.common.OneDataHeaders.ONE_DATA_TRACE_ID;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ItemClientConfig {

  @Value("${application.config.item-url}")
  String itemUrl;

  private final Tracer tracer;

  @Bean("itemClientWebClient")
  public WebClient webClient(WebClient.Builder builder) {
    return builder.baseUrl(itemUrl)
      .clientConnector(new ReactorClientHttpConnector(
        HttpClient.newConnection().compress(true))
      )
      .filter((request, next) -> {
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(request.headers());
        headers.set(ONE_DATA_CORRELATION_ID, Baggage.current().getEntryValue(ONE_DATA_CORRELATION_ID));
        headers.set(ONE_DATA_TRACE_ID, Baggage.current().getEntryValue(ONE_DATA_TRACE_ID));

        try (Scope ignored = Span.current().updateName("GET request to /api/items").makeCurrent()) {
          log.info("Start processing GET request to items endpoint");
        }

        ClientRequest newRequest = ClientRequest.from(request)
          .headers(httpHeaders -> httpHeaders.addAll(headers))
          .build();

        return next.exchange(newRequest);
      })
      .build();
  }

  @Bean
  public ItemClient itemClient(
    @Qualifier("itemClientWebClient") WebClient webClient
  ) {
    return new ItemClient(webClient);
  }

}

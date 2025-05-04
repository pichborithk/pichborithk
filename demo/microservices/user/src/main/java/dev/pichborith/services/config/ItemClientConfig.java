package dev.pichborith.services.config;

import dev.pichborith.services.integration.ItemClient;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapSetter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ItemClientConfig {

  @Value("${application.config.item-url}")
  String itemUrl;

  private static final TextMapSetter<HttpHeaders> setter = (headers, key, value) -> headers.set(key, value);

  @Bean("itemClientWebClient")
  public WebClient webClient(WebClient.Builder builder) {
    return builder.baseUrl(itemUrl)
      .clientConnector(new ReactorClientHttpConnector(
        HttpClient.newConnection().compress(true))
      )
      .filter((request, next) -> {
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(request.headers());
        headers.add("test-id", "user");

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

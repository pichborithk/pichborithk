package dev.pichborith.services.integration;

import dev.pichborith.services.model.Item;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemClient {

  WebClient webClient;

  @WithSpan
  public Flux<Item> getItems() {
    log.info("Make request to item service");
    return webClient
             .get()
             .accept(MediaType.APPLICATION_JSON)
             .retrieve()
             .bodyToFlux(Item.class);
  }
}

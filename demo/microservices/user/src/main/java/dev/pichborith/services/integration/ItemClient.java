package dev.pichborith.services.integration;

import dev.pichborith.services.model.Item;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemClient {

  WebClient webClient;

  public Flux<Item> getItems() {
    return webClient
             .get()
             .accept(MediaType.APPLICATION_JSON)
             .retrieve()
             .bodyToFlux(Item.class);
  }
}

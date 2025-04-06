package dev.pichborith.demo.repository.item;

import dev.pichborith.demo.domain.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemRepository {
    Mono<Item> save(Item item);

    Flux<Item> findAll();
}

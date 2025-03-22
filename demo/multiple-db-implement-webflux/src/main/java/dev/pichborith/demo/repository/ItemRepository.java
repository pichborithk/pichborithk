package dev.pichborith.demo.repository;

import dev.pichborith.demo.model.item.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemRepository {
    Mono<Item> save(Item item);

    Flux<Item> findAll();
}

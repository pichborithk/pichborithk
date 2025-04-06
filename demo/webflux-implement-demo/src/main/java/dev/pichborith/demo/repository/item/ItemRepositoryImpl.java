package dev.pichborith.demo.repository.item;

import dev.pichborith.demo.domain.Item;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Repository
public class ItemRepositoryImpl implements ItemRepository {

    ReactiveMongoTemplate itemMongoTemplate;

    public ItemRepositoryImpl(@Qualifier("itemMongoTemplate") ReactiveMongoTemplate itemMongoTemplate) {
        this.itemMongoTemplate = itemMongoTemplate;
    }

    @Override
    public Mono<Item> save(Item item) {
        return itemMongoTemplate.save(item);
    }

    @Override
    public Flux<Item> findAll() {
        return itemMongoTemplate.findAll(Item.class);
    }
}

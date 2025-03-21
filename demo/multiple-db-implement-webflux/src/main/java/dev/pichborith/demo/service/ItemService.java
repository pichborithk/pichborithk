package dev.pichborith.demo.service;

import dev.pichborith.demo.model.item.Item;
import dev.pichborith.demo.repository.ItemRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemService {

    ItemRepository itemRepository;

    public Mono<Item> createItem(Item item) {
        return itemRepository.save(item);
    }

    public Flux<Item> getItems() {
        return itemRepository.findAll();
    }
}

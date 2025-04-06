package dev.pichborith.demo.controller;

import dev.pichborith.demo.domain.Item;
import dev.pichborith.demo.service.ItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItemController {

    ItemService itemService;

    @PostMapping
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemService.createItem(item);
    }

    @GetMapping
    public Flux<Item> getItems() {
        return itemService.getItems();
    }
}

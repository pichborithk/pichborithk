package dev.pichborith.services.controller;

import dev.pichborith.services.domain.User;
import dev.pichborith.services.integration.ItemClient;
import dev.pichborith.services.domain.Item;
import dev.pichborith.services.service.UserService;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

  ItemClient itemClient;
  UserService userService;

  @GetMapping
  public Flux<Item> getUsers() {
    var baggage = Baggage.current().toBuilder().put("test", "test value").build();
    try (var ignored = Context.current().with(baggage).makeCurrent()) {
      log.info("Start processing GET users endpoint");
    }

    return itemClient.getItems();

  }

  @PostMapping
  public Mono<User> createUser(@RequestBody User user) {
    var baggage = Baggage.current().toBuilder().put("test", "test value").build();
    try (var ignored = Context.current().with(baggage).makeCurrent()) {
      log.info("Start processing POST users endpoint");
    }
    return userService.createUser(user);
  }


}

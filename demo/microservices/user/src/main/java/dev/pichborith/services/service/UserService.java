package dev.pichborith.services.service;

import dev.pichborith.services.domain.User;
import dev.pichborith.services.repository.UserRepository;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class
UserService {

  UserRepository userRepository;
  Tracer tracer;

  public Flux<User> getAllUsers() {
    return userRepository.findAll()
      .doOnError(failure -> System.out.println(failure.getMessage()));
  }

  public Mono<User> getUserById(String id) {
    return userRepository.findById(id)
      .doOnError(failure -> System.out.println(failure.getMessage()));
  }

  public Mono<User> createUser(User user) {
    Span span = tracer.spanBuilder("mongo.insert")
      .setParent(Context.current())
      .setSpanKind(SpanKind.SERVER)
      .setAttribute("db.system", "mongodb")
      .startSpan();

    return userRepository.findByName(user.getName())
      .contextWrite(ctx -> ctx.put("test 4", "test value 4"))
      .doOnNext(existing -> Mono.error(new IllegalStateException("Username already exists")))
      .switchIfEmpty(
        Mono.defer(
            () -> {
              var baggage = Baggage.current().toBuilder().put("test 2", "test value 2").build();
              try (var ignored = Context.root().with(baggage).makeCurrent()) {
                log.info("Start insert user to mongodb");
              }
              return userRepository.save(user);

            })
          .doOnSuccess(savedUser -> log.info("User '{}' created successfully", savedUser.getName()))
          .doOnError(failure -> log.error("Failed to create user.", failure))
      )
      .onErrorMap(failure -> new IllegalStateException("Failed to create user.", failure))
      .doFinally(signal -> span.end());
  }
}

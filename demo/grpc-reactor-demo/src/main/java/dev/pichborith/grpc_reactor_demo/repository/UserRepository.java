package dev.pichborith.grpc_reactor_demo.repository;

import dev.pichborith.grpc_reactor_demo.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

  Mono<User> findByName(String name);

}

//package dev.pichborith.grpc_demo.service;
//
//import dev.pichborith.grpc_demo.model.User;
//import dev.pichborith.grpc_demo.repository.UserRepository;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class UserService {
//
//  UserRepository userRepository;
//  Logger log = LoggerFactory.getLogger(UserService.class);
//
//  public Mono<User> createUser(User user) {
//    System.out.println(2);
//    return userRepository.findByName(user.getName())
//      .doOnNext(existing -> Mono.error(new IllegalStateException("Username already exists")))
//      .switchIfEmpty(
//        Mono.defer(() -> {
//            log.info("Start insert user to MongoDB");
//            return userRepository.save(user);
//          })
//          .doOnSuccess(savedUser -> log.info("User '{}' created successfully", savedUser.getName()))
//          .doOnError(failure -> log.error("Failed to create user.", failure))
//      )
//      .onErrorMap(failure -> new IllegalStateException("Failed to create user.", failure));
//  }
//}

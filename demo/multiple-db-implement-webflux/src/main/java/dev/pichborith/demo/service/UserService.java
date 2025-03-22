package dev.pichborith.demo.service;

import dev.pichborith.demo.model.user.User;
import dev.pichborith.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    public Flux<User> getAllUsers() {
        return userRepository.findAll()
                             .doOnError(failure -> System.out.println(
                                 failure.getMessage()));
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id)
                             .doOnError(failure -> System.out.println(
                                 failure.getMessage()));
    }

    public Mono<User> createUser(User user) {
        return userRepository.save(user)
                             .doOnError(failure -> System.out.println(
                                 failure.getMessage()));
    }
}
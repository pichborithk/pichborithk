package dev.pichborith.grpc_demo.service;

import dev.pichborith.grpc.*;
import dev.pichborith.grpc_demo.mapper.UserMapper;
import dev.pichborith.grpc_demo.model.User;
import dev.pichborith.grpc_demo.repository.UserRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

  UserRepository userRepository;
  UserMapper userMapper;

  @Override
  public void createUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
    log.info("this is request {}", request.getFullName());
    userRepository.findByName(request.getName())
      .flatMap(existing -> Mono.error(new IllegalStateException("Username already exists")))
      .switchIfEmpty(
        Mono.defer(
            () -> {
              log.info("Start insert user to mongodb");
              log.info("this is request {}", request);
              var user = userMapper.toPersistenceModel(request);
              log.info("{}", user);
              return userRepository.save(user);
            })
          .doOnSuccess(savedUser -> log.info("User '{}' created successfully", savedUser.getName()))
          .doOnError(failure -> log.error("Failed to create user.", failure))
      )
      .cast(User.class)
      .subscribe(
        savedUser -> {
          var response = UserResponse.newBuilder()
            .setId(savedUser.getId())
            .setName(savedUser.getName())
            .setRole(savedUser.getRole())
            .setFullName(savedUser.getFullName())
            .build();
          responseObserver.onNext(response);
          responseObserver.onCompleted();
        }, throwable ->
          responseObserver.onError(Status.ALREADY_EXISTS.withDescription(throwable.getMessage())
                                     .asRuntimeException())
      );
  }

  @Override
  public void getUserById(UserIdRequest request, StreamObserver<UserResponse> responseObserver) {
    userRepository.findById(request.getId())
      .subscribe(foundUser -> {
        UserResponse response = UserResponse.newBuilder()
          .setId(foundUser.getId())
          .setName(foundUser.getName())
          .setRole(foundUser.getRole())
          .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
      }, throwable -> responseObserver.onError(Status.NOT_FOUND.withDescription(throwable.getMessage())
                                                 .asRuntimeException()));
  }

}

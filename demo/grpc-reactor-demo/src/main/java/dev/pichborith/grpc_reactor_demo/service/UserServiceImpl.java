package dev.pichborith.grpc_reactor_demo.service;

import dev.pichborith.grpc.*;
import dev.pichborith.grpc_reactor_demo.repository.UserRepository;
import io.grpc.CallOptions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl extends ReactorUserServiceGrpc.UserServiceImplBase{

  UserRepository userRepository;

  @Override
  public Mono<UserResponse> createUser(Mono<UserRequest> request) {
    return super.createUser(request);
  }

  @Override
  public Mono<UserResponse> createUser(UserRequest request) {
    return super.createUser(request);
  }

  @Override
  protected CallOptions getCallOptions(int methodId) {
    return super.getCallOptions(methodId);
  }

  @Override
  public Mono<UserResponse> getUserById(Mono<UserIdRequest> request) {
    return super.getUserById(request);
  }

  @Override
  public Mono<UserResponse> getUserById(UserIdRequest request) {
    return super.getUserById(request);
  }

  @Override
  protected Throwable onErrorMap(Throwable throwable) {
    return super.onErrorMap(throwable);
  }
}

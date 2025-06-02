//package dev.pichborith.grpc_demo.grpc;
//
//import dev.pichborith.grpc.UserRequest;
//import dev.pichborith.grpc.UserResponse;
//import dev.pichborith.grpc.UserServiceGrpc;
//import dev.pichborith.grpc_demo.model.User;
//import dev.pichborith.grpc_demo.service.UserService;
//import io.grpc.Status;
//import io.grpc.stub.StreamObserver;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.grpc.server.service.GrpcService;
//import reactor.core.publisher.Mono;
//
//@GrpcService
//@Slf4j
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class UserServiceGrpcImpl extends UserServiceGrpc.UserServiceImplBase {
//
//  UserService userService;
//
//  @Override
//  public void createUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
//    System.out.println(1);
//    Mono.just(request)
//      .map(userRequest -> User.builder().name(userRequest.getName()).role(request.getRole()).build())
//      .flatMap(userService::createUser)
//      .map(user -> UserResponse.newBuilder()
//        .setId(user.getId())
//        .setName(user.getName())
//        .setRole(user.getRole())
//        .build())
//      .subscribe(
//        response -> {
//          responseObserver.onNext(response);
//          responseObserver.onCompleted();
//        }, error -> responseObserver.onError(
//          Status.ALREADY_EXISTS.withDescription(error.getMessage()).asRuntimeException()
//        )
//      );
//  }
//}

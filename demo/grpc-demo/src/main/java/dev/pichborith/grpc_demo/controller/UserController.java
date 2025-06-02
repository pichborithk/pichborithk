//package dev.pichborith.grpc_demo.controller;
//
//import dev.pichborith.grpc_demo.model.User;
//import dev.pichborith.grpc_demo.service.UserService;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/api/users")
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class UserController {
//
//  UserService userService;
//
//  @PostMapping
//  public Mono<User> createUser(@RequestBody User user) {
//    System.out.println(3);
//    return userService.createUser(user);
//  }
//}

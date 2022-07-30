package io.my.userservice.controller;

import io.my.userservice.dto.OperationType;
import io.my.userservice.dto.UserDto;
import io.my.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// crud demo

@Controller
@MessageMapping("user")
public class UserController {

    @Autowired
    private UserService service;

    // request stream
    @MessageMapping("get.all")
    public Flux<UserDto> allUsers() {
        return this.service.getAllUsers();
    }

    // request response
    @MessageMapping("get.{id}")
    public Mono<UserDto> getUserById(@DestinationVariable String id) {
        return this.service.getUserById(id);
    }

    // request response
    @MessageMapping("create")
    public Mono<UserDto> createUser(Mono<UserDto> userDtoMono) {
        return this.service.createUser(userDtoMono);
    }

    // request response
    @MessageMapping("update.{id}")
    public Mono<UserDto> updateUser(@DestinationVariable String id, Mono<UserDto> userDtoMono) {
        return this.service.updateUser(id, userDtoMono);
    }

    // fire and forget
    @MessageMapping("delete.{id}")
    public Mono<Void> deleteUser(@DestinationVariable String id) {
        return this.service.deleteUser(id);
    }

    @MessageMapping("operation.type")
    public Mono<Void> metadataOperationType(@Header("operation-type")OperationType operationType,
                                            Mono<UserDto> userDtoMono) {
        System.out.println(operationType);
        userDtoMono.doOnNext(System.out::println).subscribe();
        return Mono.empty();
    }

}

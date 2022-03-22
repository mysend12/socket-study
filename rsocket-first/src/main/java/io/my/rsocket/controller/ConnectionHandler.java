package io.my.rsocket.controller;

import io.my.rsocket.dto.ClientConnectionRequest;
import io.my.rsocket.service.MathClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class ConnectionHandler {
    @Autowired
    private MathClientManager clientManager;

    @ConnectMapping
    public Mono<Void> noEventConnection(RSocketRequester rSocketRequester) {
        System.out.println("no event connection setup");
        return Mono.empty();
    }

    @ConnectMapping("math.events.connection")
    public Mono<Void> mathEventConnection(RSocketRequester rSocketRequester) {
        System.out.println("math event connection setup");
        return Mono.fromRunnable(() -> this.clientManager.add(rSocketRequester));
//                Mono.error(new RuntimeException("invalid credentials"));
    }

}

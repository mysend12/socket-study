package io.my.rsocket.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("math.validation")
public class InputValidationController {

    @MessageMapping("double.{input}")
    public Mono<Integer> doubleIt(@DestinationVariable int input) {
        if (input < 31) {
            return Mono.just(input * 2);
        } else {
            return Mono.error(new IllegalArgumentException("can not be > 30"));
        }
    }
}

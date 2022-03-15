package io.my.rsocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@MessageMapping("math.service")
public class MathVariableController {

    @MessageMapping("print.{input}")
    public Mono<Void> print(@DestinationVariable int input) {
        log.info("Recieved : {}", input);
        return Mono.empty();
    }
}

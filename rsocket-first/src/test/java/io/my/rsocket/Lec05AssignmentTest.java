package io.my.rsocket;

import io.my.rsocket.assingment.GuessNumberResponse;
import io.my.rsocket.assingment.Player;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec05AssignmentTest {
    private RSocketRequester requester;

    @Autowired
    private RSocketRequester.Builder builder;

        @BeforeAll
    public void setUp() {
        this.requester = this.builder
                .transport(TcpClientTransport.create("localhost", 6565));
    }

    @RepeatedTest(3)
    public void assignment() {
        Player player = new Player();
        Mono<Void> mono = this.requester.route("guess.a.number")
                .data(player.guesses().delayElements(Duration.ofSeconds(1)))
                .retrieveFlux(GuessNumberResponse.class)
                .doOnNext(player.receives())
                .doFirst(player::play)
                .then();
        StepVerifier.create(mono)
                .verifyComplete()
                ;
    }

}

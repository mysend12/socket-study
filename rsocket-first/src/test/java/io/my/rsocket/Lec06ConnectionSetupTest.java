package io.my.rsocket;

import io.my.rsocket.assingment.GuessNumberResponse;
import io.my.rsocket.assingment.Player;
import io.my.rsocket.dto.ClientConnectionRequest;
import io.my.rsocket.dto.ComputationRequestDto;
import io.my.rsocket.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec06ConnectionSetupTest {
    private RSocketRequester requester;

    @Autowired
    private RSocketRequester.Builder builder;

    @BeforeAll
    public void setUp() {
        ClientConnectionRequest request = new ClientConnectionRequest();

        request.setClientId("order-service");
        request.setSecretKey("password");

        this.requester = this.builder
                .setupData(request)
                .transport(TcpClientTransport.create("localhost", 6565));
    }

    @RepeatedTest(3)
    public void connectionTest() {
        Mono<ComputationResponseDto> mono = this.requester.route("math.service.square")
                .data(new ComputationRequestDto(ThreadLocalRandom.current().nextInt(1, 50)))
                .retrieveMono(ComputationResponseDto.class)
                .doOnNext(System.out::println)
                ;

        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete()
                ;

    }
}

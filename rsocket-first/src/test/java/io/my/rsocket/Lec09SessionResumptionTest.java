package io.my.rsocket;

import io.my.rsocket.dto.ComputationRequestDto;
import io.my.rsocket.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

@SpringBootTest
@TestPropertySource(properties =
        {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"
        }
)
public class Lec09SessionResumptionTest {
    @Autowired
    private RSocketRequester.Builder builder;

    @Test
    public void connectionTest() {
        RSocketRequester requester = this.builder
                .transport(TcpClientTransport.create("localhost", 6565));

        Flux<ComputationResponseDto> flux = requester.route("math.service.table")
                .data(new ComputationRequestDto(5))
                .retrieveFlux(ComputationResponseDto.class)
                .doOnNext(System.out::println)
                ;

        StepVerifier.create(flux)
                .expectNextCount(10)
                .verifyComplete()
                ;
    }
}

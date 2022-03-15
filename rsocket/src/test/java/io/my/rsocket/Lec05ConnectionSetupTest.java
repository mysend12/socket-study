package io.my.rsocket;

import io.my.rsocket.dto.RequestDto;
import io.my.rsocket.dto.ResponseDto;
import io.my.rsocket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketClient;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec05ConnectionSetupTest {
    private RSocketClient rSocketClient;

    @BeforeAll
    public void setUp() {
        Mono<RSocket> socketMono = RSocketConnector.create()
//                .setupPayload(DefaultPayload.create("user:password"))
                .connect(TcpClientTransport.create("localhost", 6565))
                .doOnNext(r -> System.out.println("going to connect"))
                ;

        this.rSocketClient = RSocketClient.from(socketMono);


    }

    @Test
    public void connectionTest() {

        Payload payload = ObjectUtil.toPayload(new RequestDto(5));

        Flux<ResponseDto> flux = this.rSocketClient.requestStream(Mono.just(payload))
                .map(p -> ObjectUtil.toObject(p, ResponseDto.class))
                .doOnNext(System.out::println);

        StepVerifier.create(flux)
                .expectNextCount(10)
                .verifyComplete()
        ;


    }
}

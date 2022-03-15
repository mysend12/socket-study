package io.my.rsocket;

import io.my.rsocket.dto.ChartResponse;
import io.my.rsocket.dto.RequestDto;
import io.my.rsocket.dto.ResponseDto;
import io.my.rsocket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
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
public class Lec01RSocketTest {

    private RSocket rsocket;

    @BeforeAll
    public void setUp() {
        this.rsocket = RSocketConnector.create()
                .connect(TcpClientTransport.create("localhost", 6565))
                .block()
                ;
    }

    @Test
    public void fireAndForgetTest() {
        Payload payload = ObjectUtil.toPayload(new RequestDto(5));
        Mono<Void> mono = this.rsocket.fireAndForget(payload);

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void requestResponse() {
        Payload payload = ObjectUtil.toPayload(new RequestDto(5));
        Mono<ResponseDto> mono = this.rsocket.requestResponse(payload)
                .map(p -> ObjectUtil.toObject(p, ResponseDto.class))
                .doOnNext(System.out::println)
                ;

        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void requestStream() {
        Payload payload = ObjectUtil.toPayload(new RequestDto(5));
        Flux<ResponseDto> flux = this.rsocket.requestStream(payload)
                .map(p -> ObjectUtil.toObject(p, ResponseDto.class))
                .doOnNext(System.out::println)
                .take(4)
                ;

        StepVerifier.create(flux)
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void requestChannel() {
        Flux<Payload> payloadFlux = Flux.range(-10, 21)
                .delayElements(Duration.ofMillis(500))
                .map(RequestDto::new)
                .map(ObjectUtil::toPayload);

        Flux<ChartResponse> flux = this.rsocket.requestChannel(payloadFlux)
                .map(payload -> ObjectUtil.toObject(payload, ChartResponse.class))
                .doOnNext(System.out::println);

        StepVerifier.create(flux)
                .expectNextCount(21)
                .verifyComplete();
    }
}

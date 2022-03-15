package io.my.rsocket.service;

import io.my.rsocket.dto.ChartResponse;
import io.my.rsocket.dto.RequestDto;
import io.my.rsocket.dto.ResponseDto;
import io.my.rsocket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class MathService implements RSocket {


    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        System.out.println("Recieving: " + ObjectUtil.toObject(payload, RequestDto.class));

        return Mono.empty();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        return Mono.fromSupplier(() -> {
            RequestDto requestDto = ObjectUtil.toObject(payload, RequestDto.class);
            ResponseDto responseDto = new ResponseDto(
                    requestDto.getInput(),
                    requestDto.getInput() * requestDto.getInput()
            );
            return ObjectUtil.toPayload(responseDto);
        });
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        RequestDto requestDto = ObjectUtil.toObject(payload, RequestDto.class);
        return Flux.range(1, 10)
                .map(i -> i * requestDto.getInput())
                .map(i -> new ResponseDto(requestDto.getInput(), i))
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(System.out::println)
                .doFinally(System.out::println)
                .map(ObjectUtil::toPayload)
                ;
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        return Flux.from(payloads)
                .map(payload -> ObjectUtil.toObject(payload, RequestDto.class).getInput())
                .map(input -> ObjectUtil.toPayload(
                        new ChartResponse(input, (input*input) + 1))
                )
                ;
    }
}

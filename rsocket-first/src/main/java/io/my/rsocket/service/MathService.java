package io.my.rsocket.service;

import io.my.rsocket.dto.ChartResponseDto;
import io.my.rsocket.dto.ComputationRequestDto;
import io.my.rsocket.dto.ComputationResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MathService {

    // fireAndForget
    public Mono<Void> print(Mono<ComputationRequestDto> requestDtoMono) {
        return requestDtoMono.doOnNext(System.out::println)
                .then()
                ;
    }

    // requestResponse
    public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> requestDtoMono) {
        return requestDtoMono.map(ComputationRequestDto::getInput)
                .map(i -> new ComputationResponseDto(i, i * i))
                ;
    }

    // requestStream
    public Flux<ComputationResponseDto> tableStream(ComputationRequestDto dto) {
        return Flux.range(1, 10)
                .map(i -> new ComputationResponseDto(dto.getInput(), i * dto.getInput()))
                ;
    }

    // requestChannel - r^2 + 1
    public Flux<ChartResponseDto> chartStream(Flux<ComputationRequestDto> requestDtoFlux) {
        return requestDtoFlux
                .map(ComputationRequestDto::getInput)
                .map(i -> new ChartResponseDto(i, (i * i) + 1))
                ;
    }



}

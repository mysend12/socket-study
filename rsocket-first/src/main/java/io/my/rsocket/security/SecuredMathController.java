package io.my.rsocket.security;

import io.my.rsocket.dto.ChartResponseDto;
import io.my.rsocket.dto.ComputationRequestDto;
import io.my.rsocket.dto.ComputationResponseDto;
import io.my.rsocket.service.MathService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@MessageMapping("math.service.secured")
public class SecuredMathController {
    private final MathService mathService;

    @PreAuthorize("hasRole('USER')")
    @MessageMapping("square")
    public Mono<ComputationResponseDto> findSquare(
            Mono<ComputationRequestDto> requestDtoMono,
            @AuthenticationPrincipal Mono<UserDetails> userDetailsMono) {
        userDetailsMono.doOnNext(System.out::println).subscribe();
        return this.mathService.findSquare(requestDtoMono);
    }

    @MessageMapping("table")
    public Flux<ComputationResponseDto> tableStream(Mono<ComputationRequestDto> requestDtoMono) {
        return requestDtoMono.flatMapMany(this.mathService::tableStream);
    }



}

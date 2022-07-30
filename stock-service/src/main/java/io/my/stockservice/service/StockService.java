package io.my.stockservice.service;

import io.my.stockservice.dto.StockTickDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class StockService {

    private static final Stock AMAZON = new Stock(1000, "AMAZON", 20);
    private static final Stock APPLE = new Stock(100, "APPLE", 3);
    private static final Stock MICROSOFT = new Stock(200, "MICROSOFT", 5);

    public Flux<StockTickDto> getStockPrice() {
        return Flux.interval(Duration.ofSeconds(2))
                .flatMap(i -> Flux.just(AMAZON, APPLE, MICROSOFT))
                .map(s -> new StockTickDto(s.getCode(), s.getPrice()));
    }

}

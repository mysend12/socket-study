package io.my.stockservice.controller;

import io.my.stockservice.dto.StockTickDto;
import io.my.stockservice.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class StockController {

    @Autowired
    private StockService stockService;

    @MessageMapping("stock.ticks")
    public Flux<StockTickDto> stockPrice() {
        return this.stockService.getStockPrice();
    }
}

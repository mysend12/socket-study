package io.my.tradingservice.controller;

import io.my.tradingservice.client.StockClient;
import io.my.tradingservice.dto.StockTickDto;
import io.my.tradingservice.dto.StockTradeRequest;
import io.my.tradingservice.dto.StockTradeResponse;
import io.my.tradingservice.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("stock")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private StockClient stockClient;

    @GetMapping(value = "tick/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StockTickDto> stockTicks() {
        return this.stockClient.getStockStream();
    }


    @PostMapping("trade")
    public Mono<ResponseEntity<StockTradeResponse>> trade(@RequestBody Mono<StockTradeRequest> tradeRequestMono) {
        return tradeRequestMono.filter(tr -> tr.getQuantity() > 0)
                .flatMap(this.tradeService::trade)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build())
                ;
    }
}

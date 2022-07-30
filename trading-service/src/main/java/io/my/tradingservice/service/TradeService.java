package io.my.tradingservice.service;

import io.my.tradingservice.client.StockClient;
import io.my.tradingservice.client.UserClient;
import io.my.tradingservice.dto.*;
import io.my.tradingservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TradeService {

    @Autowired
    private UserStockService stockService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private StockClient stockClient;

    public Mono<StockTradeResponse> trade(StockTradeRequest tradeRequest) {
        TransactionRequest transactionRequest = EntityDtoUtil.toTransactionRequest(tradeRequest, this.estimatePrice(tradeRequest));
        Mono<StockTradeResponse> responseMono =
                TradeType.BUY.equals(tradeRequest.getTradeType()) ?
                        buyStock(tradeRequest, transactionRequest) : sellStock(tradeRequest, transactionRequest);

        return responseMono.defaultIfEmpty(EntityDtoUtil.toTradeResponse(tradeRequest, TradeStatus.FAILED, 0));
    }

    private Mono<StockTradeResponse> buyStock(StockTradeRequest tradeRequest, TransactionRequest transactionRequest) {
        return this.userClient.doTransaction(transactionRequest)
                .filter(tr -> TransactionStatus.COMPLETED.equals(tr.getStatus()))
                .flatMap(tr -> this.stockService.buyStock(tradeRequest))
                .map(us -> EntityDtoUtil.toTradeResponse(tradeRequest, TradeStatus.COMPLETE, transactionRequest.getAmount()));
    }

    private Mono<StockTradeResponse> sellStock(StockTradeRequest tradeRequest, TransactionRequest transactionRequest) {
        return this.stockService.sellStock(tradeRequest)
                .flatMap(us -> this.userClient.doTransaction(transactionRequest))
                .map(us -> EntityDtoUtil.toTradeResponse(tradeRequest, TradeStatus.COMPLETE, transactionRequest.getAmount()))
                ;
    }

    private int estimatePrice(StockTradeRequest request) {
        return request.getQuantity() * this.stockClient.getCurrentStockPrice(request.getStockSymbol());
    }
}

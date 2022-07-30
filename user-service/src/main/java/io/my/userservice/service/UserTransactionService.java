package io.my.userservice.service;

import io.my.userservice.dto.TransactionRequest;
import io.my.userservice.dto.TransactionResponse;
import io.my.userservice.dto.TransactionStatus;
import io.my.userservice.dto.TransactionType;
import io.my.userservice.entity.User;
import io.my.userservice.repository.UserRepository;
import io.my.userservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@Service
public class UserTransactionService {

    @Autowired
    private UserRepository repository;

    public Mono<TransactionResponse> doTransaction(TransactionRequest request) {
        UnaryOperator<Mono<User>> operation = TransactionType.CREDIT.equals(request.getType()) ? credit(request) : debit(request);
        return this.repository.findById(request.getUserId())
                .transform(operation)
                .flatMap(this.repository::save)
                .map(s -> EntityDtoUtil.toResponse(request, TransactionStatus.COMPLETED))
                .defaultIfEmpty(EntityDtoUtil.toResponse(request, TransactionStatus.FAILED))
        ;
    }

    private UnaryOperator<Mono<User>> credit(TransactionRequest request) {
        return userMono -> userMono.doOnNext(u -> u.setBalance(u.getBalance() + request.getAmount()));
    }

    private UnaryOperator<Mono<User>> debit(TransactionRequest request) {
        return userMono -> userMono.filter(u -> u.getBalance() >= request.getAmount())
                .doOnNext(u -> u.setBalance(u.getBalance() - request.getAmount()));
    }
}

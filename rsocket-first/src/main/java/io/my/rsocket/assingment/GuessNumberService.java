package io.my.rsocket.assingment;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class GuessNumberService {

    public Flux<GuessNumberResponse> play(Flux<Integer> flux) {
        int serverNumber = ThreadLocalRandom.current().nextInt(1, 100);

        System.out.println("Server number: " + serverNumber);
        return flux.map(i -> this.compare(serverNumber, i));
    }

    private GuessNumberResponse compare(int serverNumber, int clientNumber) {
        if (serverNumber > clientNumber) return GuessNumberResponse.GREATER;
        else if (serverNumber < clientNumber) return GuessNumberResponse.LESSER;
        else return GuessNumberResponse.EQUAL;
    }
}

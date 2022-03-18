package io.my.rsocket.dto.error;

import lombok.Getter;

@Getter
public enum StatusCode {
    EC001("given number is not within range"),
    EC002("your usage limit exceeded"),
    ;

    private final String description;

    StatusCode(String description) {
        this.description = description;
    }

}

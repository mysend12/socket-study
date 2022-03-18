package io.my.rsocket.dto;

import io.my.rsocket.dto.error.ErrorEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class Response <T> {
    ErrorEvent errorResponse;
    T successResponse;

    public Response(T successResponse) {
        this.successResponse = successResponse;
    }

    public Response(ErrorEvent errorResponse) {
        this.errorResponse = errorResponse;
    }

    public boolean hasError() {
        return Objects.nonNull(this.errorResponse);
    }

    public static <T> Response<T> with(T t) {
        return new Response<>(t);
    }

    public static <T> Response<T> with(ErrorEvent errorResponse) {
        return new Response<>(errorResponse);
    }


}

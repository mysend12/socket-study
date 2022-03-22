package io.my.rsocket.dto;

import lombok.Data;

@Data
public class ClientConnectionRequest {
    private String clientId;
    private String secretKey;
}

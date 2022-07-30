package io.my.userservice.util;

import io.my.userservice.dto.TransactionRequest;
import io.my.userservice.dto.TransactionResponse;
import io.my.userservice.dto.TransactionStatus;
import io.my.userservice.dto.UserDto;
import io.my.userservice.entity.User;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    public static User toEntity(UserDto dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        return user;
    }

    public static TransactionResponse toResponse(TransactionRequest request, TransactionStatus status) {
        TransactionResponse response = new TransactionResponse();
        response.setAmount(request.getAmount());
        response.setType(request.getType());
        response.setUserId(request.getUserId());
        response.setStatus(status);
        return response;
    }
}

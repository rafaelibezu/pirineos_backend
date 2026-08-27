package com.lospirineos.api.user.mapper;

import com.lospirineos.api.user.dto.response.UserResponse;
import com.lospirineos.api.user.entity.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().getId(),
                user.getRole().getCode(),
                user.getRole().getName(),
                user.getActive()
        );
    }
}
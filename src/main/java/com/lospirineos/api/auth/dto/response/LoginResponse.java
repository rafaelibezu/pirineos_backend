package com.lospirineos.api.auth.dto.response;

public record LoginResponse(

        String accessToken,

        String tokenType,

        Long userId,

        String username,

        String firstName,

        String lastName,

        String role
) {
}
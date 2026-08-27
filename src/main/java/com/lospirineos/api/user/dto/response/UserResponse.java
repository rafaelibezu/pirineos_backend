package com.lospirineos.api.user.dto.response;

public record UserResponse(
        Long id,
        String username,
        String firstName,
        String lastName,
        Long roleId,
        String roleCode,
        String roleName,
        Boolean active
) {
}
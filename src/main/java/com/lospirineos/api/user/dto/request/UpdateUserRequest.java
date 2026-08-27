package com.lospirineos.api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String firstName,

        @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
        String lastName,

        @NotNull(message = "El rol es obligatorio")
        Long roleId,

        @NotNull(message = "El estado es obligatorio")
        Boolean active
) {
}
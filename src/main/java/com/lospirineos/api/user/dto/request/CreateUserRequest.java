package com.lospirineos.api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(max = 80, message = "El nombre de usuario no puede superar los 80 caracteres")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        String password,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String firstName,

        @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
        String lastName,

        @NotNull(message = "El rol es obligatorio")
        Long roleId
) {
}
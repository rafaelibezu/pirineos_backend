package com.lospirineos.api.cashregister.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCashRegisterRequest(

        @NotBlank(message = "El código de la caja es obligatorio")
        @Size(max = 50, message = "El código no puede superar los 50 caracteres")
        String code,

        @NotBlank(message = "El nombre de la caja es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        @Size(max = 255, message = "La descripción no puede superar los 255 caracteres")
        String description
) {
}
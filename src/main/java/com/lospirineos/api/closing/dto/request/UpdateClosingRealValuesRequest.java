package com.lospirineos.api.closing.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateClosingRealValuesRequest(

        @NotNull(message = "El importe de monedas es obligatorio")
        @DecimalMin(value = "0.00", message = "Las monedas no pueden ser negativas")
        BigDecimal coins,

        @NotNull(message = "El importe de billetes es obligatorio")
        @DecimalMin(value = "0.00", message = "Los billetes no pueden ser negativos")
        BigDecimal banknotes,

        @NotNull(message = "El TPV real es obligatorio")
        @DecimalMin(value = "0.00", message = "El TPV no puede ser negativo")
        BigDecimal realPos,

        String cashierPhotoUrl
) {
}
package com.lospirineos.api.closing.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateDailyClosingRequest(

        @NotNull(message = "El turno es obligatorio")
        Long shiftId,

        @NotNull(message = "La caja es obligatoria")
        Long cashRegisterId,

        @NotNull(message = "El camarero es obligatorio")
        Long waiterId,

        @NotNull(message = "El cierre Z es obligatorio")
        @DecimalMin(value = "0.00", message = "El cierre Z no puede ser negativo")
        BigDecimal zClosing,

        @NotNull(message = "El importe manual es obligatorio")
        @DecimalMin(value = "0.00", message = "El importe manual no puede ser negativo")
        BigDecimal manualAmount,

        @NotNull(message = "El importe de Visas es obligatorio")
        @DecimalMin(value = "0.00", message = "El importe de Visas no puede ser negativo")
        BigDecimal declaredVisa,

        String closingPhotoUrl
) {
}
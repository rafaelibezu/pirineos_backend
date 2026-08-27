package com.lospirineos.api.transfer.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateCashTransferRequest(

        @NotNull(message = "El turno es obligatorio")
        Long shiftId,

        @NotNull(message = "La caja de origen es obligatoria")
        Long sourceCashRegisterId,

        @NotNull(message = "La caja de destino es obligatoria")
        Long destinationCashRegisterId,

        @NotNull(message = "El importe es obligatorio")
        @DecimalMin(
                value = "0.01",
                message = "El importe debe ser mayor a 0"
        )
        BigDecimal amount,

        @Size(
                max = 255,
                message = "El motivo no puede superar los 255 caracteres"
        )
        String reason
) {
}
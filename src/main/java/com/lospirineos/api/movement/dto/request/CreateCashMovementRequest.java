package com.lospirineos.api.movement.dto.request;

import com.lospirineos.api.common.enums.AccountType;
import com.lospirineos.api.common.enums.MovementType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateCashMovementRequest(

        @NotNull(message = "El turno es obligatorio")
        Long shiftId,

        @NotNull(message = "El tipo de movimiento es obligatorio")
        MovementType movementType,

        @NotBlank(message = "El concepto es obligatorio")
        @Size(
                max = 150,
                message = "El concepto no puede superar los 150 caracteres"
        )
        String concept,

        @NotNull(message = "La cuenta es obligatoria")
        AccountType account,

        @Size(
                max = 150,
                message = "El beneficiario no puede superar los 150 caracteres"
        )
        String beneficiary,

        @NotNull(message = "El importe es obligatorio")
        @DecimalMin(
                value = "0.01",
                message = "El importe debe ser mayor a 0"
        )
        BigDecimal amount,

        @Size(
                max = 500,
                message = "La URL de la fotografía no puede superar los 500 caracteres"
        )
        String photoUrl
) {
}
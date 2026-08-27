package com.lospirineos.api.payment.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateClosingPaymentRequest(

        @NotNull(message = "La fecha de factura es obligatoria")
        LocalDate invoiceDate,

        @NotNull(message = "El distribuidor es obligatorio")
        Long distributorId,

        @NotNull(message = "La cantidad es obligatoria")
        @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor que cero")
        BigDecimal amount,

        String photoUrl
) {
}
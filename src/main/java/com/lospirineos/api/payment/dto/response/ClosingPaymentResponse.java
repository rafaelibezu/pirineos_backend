package com.lospirineos.api.payment.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClosingPaymentResponse(
        Long id,
        LocalDate invoiceDate,
        Long distributorId,
        String distributorName,
        BigDecimal amount,
        String photoUrl
) {
}
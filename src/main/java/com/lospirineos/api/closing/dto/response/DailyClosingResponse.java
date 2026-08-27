package com.lospirineos.api.closing.dto.response;

import com.lospirineos.api.payment.dto.response.ClosingPaymentResponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record DailyClosingResponse(

        Long id,

        OffsetDateTime closingAt,

        Long shiftId,
        String shiftName,

        Long cashRegisterId,
        String cashRegisterName,

        Long waiterId,
        String waiterName,

        BigDecimal zClosing,
        BigDecimal manualAmount,
        BigDecimal declaredVisa,

        BigDecimal totalPayments,

        String closingPhotoUrl,

        BigDecimal coins,
        BigDecimal banknotes,
        BigDecimal realPos,

        BigDecimal cashTotal,
        BigDecimal expectedCash,

        BigDecimal cashDifference,
        BigDecimal posDifference,

        String cashierPhotoUrl,

        String status,

        List<ClosingPaymentResponse> payments
) {
}
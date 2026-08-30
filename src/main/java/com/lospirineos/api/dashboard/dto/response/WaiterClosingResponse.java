package com.lospirineos.api.dashboard.dto.response;

import com.lospirineos.api.common.enums.ClosingStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record WaiterClosingResponse(

        Long closingId,

        OffsetDateTime closingAt,

        Long shiftId,
        String shiftName,

        Long cashRegisterId,
        String cashRegisterName,

        BigDecimal zClosing,
        BigDecimal manualAmount,
        BigDecimal declaredVisa,
        BigDecimal totalPayments,

        BigDecimal cashTotal,
        BigDecimal realPos,

        BigDecimal cashDifference,
        BigDecimal posDifference,
        BigDecimal totalDifference,

        ClosingStatus status
) {
}
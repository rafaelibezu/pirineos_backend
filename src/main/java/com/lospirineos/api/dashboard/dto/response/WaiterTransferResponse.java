package com.lospirineos.api.dashboard.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record WaiterTransferResponse(

        Long transferId,

        OffsetDateTime transferAt,

        String shiftName,

        Long sourceCashRegisterId,

        String sourceCashRegisterName,

        Long destinationCashRegisterId,

        String destinationCashRegisterName,

        BigDecimal amount,

        String reason
) {
}
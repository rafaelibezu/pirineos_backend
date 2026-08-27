package com.lospirineos.api.transfer.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CashTransferResponse(

        Long id,
        OffsetDateTime transferAt,

        Long shiftId,
        String shiftName,

        Long responsibleUserId,
        String responsibleUserName,

        Long sourceCashRegisterId,
        String sourceCashRegisterName,

        Long destinationCashRegisterId,
        String destinationCashRegisterName,

        BigDecimal amount,
        String reason
) {
}
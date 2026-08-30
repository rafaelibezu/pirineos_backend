package com.lospirineos.api.dashboard.dto.response;

import com.lospirineos.api.common.enums.AccountType;
import com.lospirineos.api.common.enums.MovementType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record WaiterMovementResponse(

        Long movementId,

        OffsetDateTime movementAt,

        MovementType movementType,

        String concept,

        AccountType account,

        String accountName,

        String beneficiary,

        BigDecimal amount,

        String shiftName
) {
}
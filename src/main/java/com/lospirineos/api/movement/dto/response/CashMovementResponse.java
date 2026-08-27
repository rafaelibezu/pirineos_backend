package com.lospirineos.api.movement.dto.response;

import com.lospirineos.api.common.enums.AccountType;
import com.lospirineos.api.common.enums.MovementType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CashMovementResponse(

        Long id,
        OffsetDateTime movementAt,

        Long shiftId,
        String shiftName,

        Long userId,
        String userName,

        MovementType movementType,

        String concept,

        AccountType account,
        String accountName,

        String beneficiary,

        BigDecimal amount,

        String photoUrl
) {
}
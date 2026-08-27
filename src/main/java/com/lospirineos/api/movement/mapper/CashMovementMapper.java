package com.lospirineos.api.movement.mapper;

import com.lospirineos.api.movement.dto.response.CashMovementResponse;
import com.lospirineos.api.movement.entity.CashMovement;

public final class CashMovementMapper {

    private CashMovementMapper() {
    }

    public static CashMovementResponse toResponse(
            CashMovement movement
    ) {

        String userName =
                movement.getUser().getFirstName()
                        + (
                        movement.getUser().getLastName() != null
                                ? " " + movement.getUser().getLastName()
                                : ""
                );

        return new CashMovementResponse(
                movement.getId(),
                movement.getMovementAt(),

                movement.getShift().getId(),
                movement.getShift().getName(),

                movement.getUser().getId(),
                userName,

                movement.getMovementType(),

                movement.getConcept(),

                movement.getAccount(),
                movement.getAccount().getDisplayName(),

                movement.getBeneficiary(),

                movement.getAmount(),

                movement.getPhotoUrl()
        );
    }
}
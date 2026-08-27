package com.lospirineos.api.transfer.mapper;

import com.lospirineos.api.transfer.dto.response.CashTransferResponse;
import com.lospirineos.api.transfer.entity.CashTransfer;

public final class CashTransferMapper {

    private CashTransferMapper() {
    }

    public static CashTransferResponse toResponse(
            CashTransfer transfer
    ) {

        String responsibleUserName =
                transfer.getResponsibleUser().getFirstName()
                        + (
                        transfer.getResponsibleUser().getLastName() != null
                                ? " " + transfer.getResponsibleUser().getLastName()
                                : ""
                );

        return new CashTransferResponse(
                transfer.getId(),
                transfer.getTransferAt(),

                transfer.getShift().getId(),
                transfer.getShift().getName(),

                transfer.getResponsibleUser().getId(),
                responsibleUserName,

                transfer.getSourceCashRegister().getId(),
                transfer.getSourceCashRegister().getName(),

                transfer.getDestinationCashRegister().getId(),
                transfer.getDestinationCashRegister().getName(),

                transfer.getAmount(),
                transfer.getReason()
        );
    }
}
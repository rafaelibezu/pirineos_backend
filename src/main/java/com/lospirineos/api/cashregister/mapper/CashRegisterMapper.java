package com.lospirineos.api.cashregister.mapper;

import com.lospirineos.api.cashregister.dto.response.CashRegisterResponse;
import com.lospirineos.api.cashregister.entity.CashRegister;

public final class CashRegisterMapper {

    private CashRegisterMapper() {
    }

    public static CashRegisterResponse toResponse(
            CashRegister cashRegister
    ) {

        return new CashRegisterResponse(
                cashRegister.getId(),
                cashRegister.getCode(),
                cashRegister.getName(),
                cashRegister.getDescription(),
                cashRegister.getActive()
        );
    }
}
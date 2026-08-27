package com.lospirineos.api.cashregister.dto.response;

public record CashRegisterResponse(
        Long id,
        String code,
        String name,
        String description,
        Boolean active
) {
}
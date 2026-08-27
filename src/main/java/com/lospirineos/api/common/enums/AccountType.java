package com.lospirineos.api.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {

    CASHIER("Cajero"),
    BANK("Banco"),
    PIRI_FUND("Fondo Piri"),
    SLOT_MACHINE("Tragaperra");

    private final String displayName;
}
package com.lospirineos.api.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseStatus {

    SUCCESS(
            "SUCCESS",
            "Operación realizada correctamente"
    ),

    ERROR(
            "ERROR",
            "Ocurrió un error"
    );

    private final String code;
    private final String message;
}
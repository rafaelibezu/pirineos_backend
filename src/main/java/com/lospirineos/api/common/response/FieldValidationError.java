package com.lospirineos.api.common.response;

public record FieldValidationError(
        String field,
        String message
) {
}
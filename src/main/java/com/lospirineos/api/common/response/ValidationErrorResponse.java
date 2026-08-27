package com.lospirineos.api.common.response;

import java.util.List;

public record ValidationErrorResponse(
        String status,
        String message,
        List<FieldValidationError> errors
) {
}
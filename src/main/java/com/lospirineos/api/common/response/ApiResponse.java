package com.lospirineos.api.common.response;

import com.lospirineos.api.common.enums.ResponseStatus;

public record ApiResponse<T>(
        String status,
        String message,
        T data
) {

    public static <T> ApiResponse<T> of(
            String status,
            String message,
            T data
    ) {
        return new ApiResponse<>(
                status,
                message,
                data
        );
    }

    public static <T> ApiResponse<T> success(
            T data
    ) {
        return new ApiResponse<>(
                ResponseStatus.SUCCESS.getCode(),
                ResponseStatus.SUCCESS.getMessage(),
                data
        );
    }

    public static <T> ApiResponse<T> success(
            String message,
            T data
    ) {
        return new ApiResponse<>(
                ResponseStatus.SUCCESS.getCode(),
                message,
                data
        );
    }

    public static <T> ApiResponse<T> error(
            String message
    ) {
        return new ApiResponse<>(
                ResponseStatus.ERROR.getCode(),
                message,
                null
        );
    }
}
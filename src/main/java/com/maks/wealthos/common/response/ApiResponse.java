package com.maks.wealthos.common.response;

public record ApiResponse<T>(
        String message,
        T data
) {
}

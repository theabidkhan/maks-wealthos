package com.maks.wealthos.common.exception;

import com.maks.wealthos.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception ex) {

        return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(
                        "Unexpected error occurred",
                        ex.getMessage()
                ));
    }
}


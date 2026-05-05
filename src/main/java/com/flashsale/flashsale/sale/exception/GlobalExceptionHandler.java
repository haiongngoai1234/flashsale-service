package com.flashsale.flashsale.sale.exception;

import com.flashsale.flashsale.sale.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                // add field data if u want or follow to document
                .body(new ApiResponse<>(ex.getMessage()));
    }
}

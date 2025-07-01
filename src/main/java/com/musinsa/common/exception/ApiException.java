package com.musinsa.common.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
} 
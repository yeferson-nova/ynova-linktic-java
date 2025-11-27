package com.ynova.core.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> params;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.params = Map.of();
    }

    public BusinessException(ErrorCode errorCode, Map<String, Object> params) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.params = params;
    }
}

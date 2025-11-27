package com.ynova.core.exception;

import com.ynova.core.api.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex,
            HttpServletRequest request) {
        log.warn("BusinessException: code={}, message={}", ex.getErrorCode().getCode(), ex.getMessage());

        Map<String, Object> errorDetail = Map.of(
                "status", ex.getErrorCode().getHttpStatus().value(),
                "code", ex.getErrorCode().getCode(),
                "title", ex.getErrorCode().getMessage(),
                "detail", ex.getParams().toString(),
                "traceId", MDC.get("traceId") != null ? MDC.get("traceId") : "N/A");

        ApiResponse<Object> response = ApiResponse.builder()
                .errors(List.of(errorDetail))
                .meta(Map.of("timestamp", LocalDateTime.now()))
                .build();

        return new ResponseEntity<>(response, ex.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error: ", ex);

        Map<String, Object> errorDetail = Map.of(
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "code", "INTERNAL_SERVER_ERROR",
                "title", "An unexpected error occurred",
                "detail", ex.getMessage(),
                "traceId", MDC.get("traceId") != null ? MDC.get("traceId") : "N/A");

        ApiResponse<Object> response = ApiResponse.builder()
                .errors(List.of(errorDetail))
                .meta(Map.of("timestamp", LocalDateTime.now()))
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

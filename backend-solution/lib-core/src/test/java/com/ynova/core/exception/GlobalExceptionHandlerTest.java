package com.ynova.core.exception;

import com.ynova.core.api.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleBusinessException_ShouldReturnCorrectResponse() {
        // Arrange
        ErrorCode errorCode = new ErrorCode() {
            @Override
            public String getCode() {
                return "TEST_ERROR";
            }

            @Override
            public String getMessage() {
                return "Test error message";
            }

            @Override
            public HttpStatus getHttpStatus() {
                return HttpStatus.BAD_REQUEST;
            }
        };

        BusinessException ex = new BusinessException(errorCode, Map.of("param", "value"));
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Act
        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleBusinessException(ex, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> errors = (List<Map<String, Object>>) response.getBody().getErrors();
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
        assertEquals("TEST_ERROR", errors.get(0).get("code"));
        assertEquals("Test error message", errors.get(0).get("title"));
    }

    @Test
    void handleGeneralException_ShouldReturnInternalServerError() {
        // Arrange
        Exception ex = new RuntimeException("Unexpected error");
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Act
        ResponseEntity<ApiResponse<Object>> response = exceptionHandler.handleGenericException(ex, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> errors = (List<Map<String, Object>>) response.getBody().getErrors();
        assertNotNull(errors);
        assertFalse(errors.isEmpty());
        assertEquals("INTERNAL_SERVER_ERROR", errors.get(0).get("code"));
    }
}

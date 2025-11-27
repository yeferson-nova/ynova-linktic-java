package com.ynova.core.api;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void builder_ShouldCreateCorrectInstance() {
        // Arrange
        String data = "test data";
        Map<String, Object> meta = Map.of("page", 1);
        Map<String, Object> error = Map.of(
                "code", "ERR_001",
                "detail", "Error detail");

        // Act
        ApiResponse<String> response = ApiResponse.<String>builder()
                .data(data)
                .meta(meta)
                .errors(error)
                .build();

        // Assert
        assertEquals(data, response.getData());
        assertEquals(meta, response.getMeta());
        assertNotNull(response.getErrors());
        assertTrue(response.getErrors() instanceof Map);
        assertEquals("ERR_001", ((Map<?, ?>) response.getErrors()).get("code"));
    }
}

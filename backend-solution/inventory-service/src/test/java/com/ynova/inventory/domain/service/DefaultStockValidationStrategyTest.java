package com.ynova.inventory.domain.service;

import com.ynova.inventory.domain.strategy.DefaultStockValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultStockValidationStrategyTest {

    private DefaultStockValidationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new DefaultStockValidationStrategy();
    }

    @Test
    void validateStock_ShouldReturnTrue_WhenQuantityIsPositive() {
        assertTrue(strategy.validateStock(1L, 10));
    }

    @Test
    void validateStock_ShouldReturnFalse_WhenQuantityIsZeroOrNegative() {
        assertFalse(strategy.validateStock(1L, 0));
        assertFalse(strategy.validateStock(1L, -5));
    }
}

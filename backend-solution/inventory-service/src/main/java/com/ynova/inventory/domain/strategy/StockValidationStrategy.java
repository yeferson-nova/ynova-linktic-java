package com.ynova.inventory.domain.strategy;

public interface StockValidationStrategy {
    boolean validateStock(Long productId, Integer quantity);
}

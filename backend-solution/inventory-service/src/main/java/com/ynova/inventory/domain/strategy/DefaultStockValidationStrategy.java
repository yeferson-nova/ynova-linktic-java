package com.ynova.inventory.domain.strategy;

import org.springframework.stereotype.Component;

@Component
public class DefaultStockValidationStrategy implements StockValidationStrategy {

    @Override
    public boolean validateStock(Long productId, Integer quantity) {

        return quantity > 0;
    }
}

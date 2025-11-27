package com.ynova.inventory.domain.port.in;

import com.ynova.inventory.domain.model.Inventory;

public interface UpdateStockUseCase {
    Inventory updateStock(Long productId, Integer quantity);
}

package com.ynova.inventory.domain.port.out;

import com.ynova.inventory.domain.model.Inventory;
import java.util.Optional;

public interface InventoryRepositoryPort {
    Optional<Inventory> findByProductId(Long productId);

    Inventory save(Inventory inventory);
}

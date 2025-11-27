package com.ynova.inventory.infrastructure.adapter.out.persistence;

import com.ynova.inventory.domain.model.Inventory;
import com.ynova.inventory.domain.port.out.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements InventoryRepositoryPort {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public Optional<Inventory> findByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .map(inventoryMapper::toDomain);
    }

    @Override
    public Inventory save(Inventory inventory) {
        InventoryEntity entity = inventoryMapper.toEntity(inventory);
        InventoryEntity savedEntity = inventoryRepository.save(entity);
        return inventoryMapper.toDomain(savedEntity);
    }
}

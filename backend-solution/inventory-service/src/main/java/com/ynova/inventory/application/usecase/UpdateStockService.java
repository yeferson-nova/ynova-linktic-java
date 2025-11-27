package com.ynova.inventory.application.usecase;

import com.ynova.inventory.domain.event.InventoryLowEvent;
import com.ynova.inventory.domain.model.Inventory;
import com.ynova.inventory.domain.port.in.UpdateStockUseCase;
import com.ynova.inventory.domain.port.out.InventoryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateStockService implements UpdateStockUseCase {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Inventory updateStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepositoryPort.findByProductId(productId)
                .orElse(Inventory.builder()
                        .productId(productId)
                        .quantity(0)
                        .build());

        int newQuantity = inventory.getQuantity() + quantity;
        if (newQuantity < 0) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setQuantity(newQuantity);
        Inventory savedInventory = inventoryRepositoryPort.save(inventory);

        if (newQuantity < 5) {
            eventPublisher.publishEvent(new InventoryLowEvent(
                    productId,
                    newQuantity,
                    "Stock is low for product " + productId));
        }

        return savedInventory;
    }
}

package com.ynova.inventory.application.usecase;

import com.ynova.inventory.domain.event.InventoryLowEvent;
import com.ynova.inventory.domain.model.Inventory;
import com.ynova.inventory.domain.port.out.InventoryRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateStockServiceTest {

    @Mock
    private InventoryRepositoryPort inventoryRepositoryPort;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UpdateStockService updateStockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateStock_ShouldUpdateAndPublishEvent_WhenLowStock() {
        // Arrange
        Long productId = 1L;
        Integer quantityToAdd = -96; // 100 - 96 = 4 (Low stock)
        Inventory inventory = Inventory.builder().productId(productId).quantity(100).build();

        when(inventoryRepositoryPort.findByProductId(productId)).thenReturn(Optional.of(inventory));
        when(inventoryRepositoryPort.save(any(Inventory.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Inventory result = updateStockService.updateStock(productId, quantityToAdd);

        // Assert
        assertEquals(4, result.getQuantity());
        verify(inventoryRepositoryPort).save(inventory);
        verify(eventPublisher).publishEvent(any(InventoryLowEvent.class));
    }
}

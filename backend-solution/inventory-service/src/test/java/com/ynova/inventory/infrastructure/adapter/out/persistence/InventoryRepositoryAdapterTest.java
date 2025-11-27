package com.ynova.inventory.infrastructure.adapter.out.persistence;

import com.ynova.inventory.domain.model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryRepositoryAdapterTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @InjectMocks
    private InventoryRepositoryAdapter inventoryRepositoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByProductId_ShouldReturnInventory() {
        Long productId = 1L;
        InventoryEntity entity = new InventoryEntity();
        Inventory inventory = new Inventory();
        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(entity));
        when(inventoryMapper.toDomain(entity)).thenReturn(inventory);

        Optional<Inventory> result = inventoryRepositoryAdapter.findByProductId(productId);

        assertTrue(result.isPresent());
        verify(inventoryRepository).findByProductId(productId);
    }
}

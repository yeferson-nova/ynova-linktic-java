package com.ynova.inventory.infrastructure.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynova.inventory.domain.model.Inventory;
import com.ynova.inventory.domain.port.in.UpdateStockUseCase;
import com.ynova.inventory.domain.port.out.InventoryRepositoryPort;
import com.ynova.inventory.domain.strategy.StockValidationStrategy;
import com.ynova.inventory.infrastructure.adapter.out.client.ProductClient;
import com.ynova.inventory.infrastructure.adapter.out.client.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductClient productClient;

    @MockBean
    private StockValidationStrategy stockValidationStrategy;

    @MockBean
    private InventoryRepositoryPort inventoryRepositoryPort;

    @MockBean
    private UpdateStockUseCase updateStockUseCase;

    @Test
    void checkInventory_ShouldReturnStockInfo() throws Exception {
        when(productClient.getProduct(1L)).thenReturn(new ProductDto(1L, "Test", "Desc", BigDecimal.valueOf(100.0)));
        when(inventoryRepositoryPort.findByProductId(1L))
                .thenReturn(Optional.of(Inventory.builder().quantity(10).build()));
        when(stockValidationStrategy.validateStock(1L, 10)).thenReturn(true);

        mockMvc.perform(get("/api/v1/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.inStock").value(true));
    }

    @Test
    void updateStock_ShouldReturnUpdatedInventory() throws Exception {
        Inventory inventory = Inventory.builder().productId(1L).quantity(50).build();
        when(updateStockUseCase.updateStock(1L, 50)).thenReturn(inventory);

        mockMvc.perform(post("/api/v1/inventory/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("productId", 1, "quantity", 50))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.quantity").value(50));
    }
}

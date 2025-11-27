package com.ynova.inventory.infrastructure.adapter.in.web;

import com.ynova.core.api.ApiResponse;
import com.ynova.inventory.domain.strategy.StockValidationStrategy;
import com.ynova.inventory.infrastructure.adapter.out.client.ProductClient;
import com.ynova.inventory.infrastructure.adapter.out.client.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

        private final ProductClient productClient;
        private final StockValidationStrategy stockValidationStrategy;
        private final com.ynova.inventory.domain.port.out.InventoryRepositoryPort inventoryRepositoryPort;
        private final com.ynova.inventory.domain.port.in.UpdateStockUseCase updateStockUseCase;

        @GetMapping("/{productId}")
        public ResponseEntity<ApiResponse<Map<String, Object>>> checkInventory(@PathVariable Long productId) {
                ProductDto product = productClient.getProduct(productId);

                if (product == null) {
                        return ResponseEntity.notFound().build();
                }

                Integer quantity = inventoryRepositoryPort.findByProductId(productId)
                                .map(com.ynova.inventory.domain.model.Inventory::getQuantity)
                                .orElse(0);

                boolean inStock = stockValidationStrategy.validateStock(productId, quantity);

                ApiResponse<Map<String, Object>> response = ApiResponse.<Map<String, Object>>builder()
                                .data(Map.of(
                                                "productId", product.getId(),
                                                "productName", product.getName(),
                                                "quantity", quantity,
                                                "inStock", inStock))
                                .meta(Map.of("message", "Inventory checked successfully"))
                                .build();

                return ResponseEntity.ok(response);
        }

        @org.springframework.web.bind.annotation.PostMapping("/update")
        public ResponseEntity<ApiResponse<com.ynova.inventory.domain.model.Inventory>> updateStock(
                        @org.springframework.web.bind.annotation.RequestBody Map<String, Object> payload) {

                Long productId = Long.valueOf(payload.get("productId").toString());
                Integer quantity = Integer.valueOf(payload.get("quantity").toString());

                com.ynova.inventory.domain.model.Inventory updatedInventory = updateStockUseCase.updateStock(productId,
                                quantity);

                ApiResponse<com.ynova.inventory.domain.model.Inventory> response = ApiResponse.<com.ynova.inventory.domain.model.Inventory>builder()
                                .data(updatedInventory)
                                .meta(Map.of("message", "Stock updated successfully"))
                                .build();

                return ResponseEntity.ok(response);
        }
}

package com.ynova.inventory.domain.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryLowEvent {
    private Long productId;
    private Integer currentQuantity;
    private String message;
}

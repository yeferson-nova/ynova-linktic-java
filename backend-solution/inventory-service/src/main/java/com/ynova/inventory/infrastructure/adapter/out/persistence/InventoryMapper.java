package com.ynova.inventory.infrastructure.adapter.out.persistence;

import com.ynova.inventory.domain.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    Inventory toDomain(InventoryEntity entity);

    InventoryEntity toEntity(Inventory domain);
}

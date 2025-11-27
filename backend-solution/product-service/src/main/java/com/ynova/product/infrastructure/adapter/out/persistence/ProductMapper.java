package com.ynova.product.infrastructure.adapter.out.persistence;

import com.ynova.product.domain.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toDomain(ProductEntity entity);

    ProductEntity toEntity(Product domain);
}

package com.ynova.product.domain.port.in;

import com.ynova.product.domain.model.Product;

public interface GetProductUseCase {
    Product getProduct(Long id);
}

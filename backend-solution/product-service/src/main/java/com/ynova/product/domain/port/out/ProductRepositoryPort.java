package com.ynova.product.domain.port.out;

import com.ynova.product.domain.model.Product;

public interface ProductRepositoryPort {
    Product save(Product product);

    Product findById(Long id);

    void deleteById(Long id);

    java.util.List<Product> findAll(int page, int size);
}

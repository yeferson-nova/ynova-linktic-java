package com.ynova.product.application.usecase;

import com.ynova.product.domain.model.Product;
import com.ynova.product.domain.port.in.ListProductsUseCase;
import com.ynova.product.domain.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListProductsService implements ListProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public List<Product> listProducts(int page, int size) {
        return productRepositoryPort.findAll(page, size);
    }
}

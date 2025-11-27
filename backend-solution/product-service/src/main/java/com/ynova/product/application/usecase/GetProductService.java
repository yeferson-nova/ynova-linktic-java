package com.ynova.product.application.usecase;

import com.ynova.product.domain.model.Product;
import com.ynova.product.domain.port.in.GetProductUseCase;
import com.ynova.product.domain.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductService implements GetProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public Product getProduct(Long id) {
        return productRepositoryPort.findById(id);
    }
}

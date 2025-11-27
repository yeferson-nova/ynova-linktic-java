package com.ynova.product.application.usecase;

import com.ynova.product.domain.port.in.DeleteProductUseCase;
import com.ynova.product.domain.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProductService implements DeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public void deleteProduct(Long id) {

        productRepositoryPort.findById(id);
        productRepositoryPort.deleteById(id);
    }
}

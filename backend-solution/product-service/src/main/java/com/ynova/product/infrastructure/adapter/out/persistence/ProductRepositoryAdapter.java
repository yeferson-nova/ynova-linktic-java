package com.ynova.product.infrastructure.adapter.out.persistence;

import com.ynova.product.domain.model.Product;
import com.ynova.product.domain.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Product save(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity savedEntity = productRepository.save(entity);
        return productMapper.toDomain(savedEntity);
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDomain)
                .orElseThrow(() -> new com.ynova.core.exception.BusinessException(
                        new com.ynova.core.exception.ErrorCode() {
                            @Override
                            public String getCode() {
                                return "PRODUCT_NOT_FOUND";
                            }

                            @Override
                            public String getMessage() {
                                return "Product not found with id: %s";
                            }

                            @Override
                            public org.springframework.http.HttpStatus getHttpStatus() {
                                return org.springframework.http.HttpStatus.NOT_FOUND;
                            }
                        },
                        java.util.Map.of("id", id)));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public java.util.List<Product> findAll(int page, int size) {
        return productRepository.findAll(org.springframework.data.domain.PageRequest.of(page, size))
                .stream()
                .map(productMapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }
}

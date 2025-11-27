package com.ynova.product.infrastructure.adapter.out.persistence;

import com.ynova.product.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductRepositoryAdapterTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductRepositoryAdapter productRepositoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldSaveAndReturnProduct() {
        Product product = new Product();
        ProductEntity entity = new ProductEntity();
        when(productMapper.toEntity(product)).thenReturn(entity);
        when(productRepository.save(entity)).thenReturn(entity);
        when(productMapper.toDomain(entity)).thenReturn(product);

        Product result = productRepositoryAdapter.save(product);

        assertNotNull(result);
        verify(productRepository).save(entity);
    }

    @Test
    void findById_ShouldReturnProduct() {
        Long id = 1L;
        ProductEntity entity = new ProductEntity();
        Product product = new Product();
        when(productRepository.findById(id)).thenReturn(Optional.of(entity));
        when(productMapper.toDomain(entity)).thenReturn(product);

        Product result = productRepositoryAdapter.findById(id);

        assertNotNull(result);
        verify(productRepository).findById(id);
    }

    @Test
    void findAll_ShouldReturnList() {
        int page = 0;
        int size = 10;
        Page<ProductEntity> pageResult = new PageImpl<>(List.of(new ProductEntity()));
        when(productRepository.findAll(PageRequest.of(page, size))).thenReturn(pageResult);
        when(productMapper.toDomain(any())).thenReturn(new Product());

        List<Product> result = productRepositoryAdapter.findAll(page, size);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}

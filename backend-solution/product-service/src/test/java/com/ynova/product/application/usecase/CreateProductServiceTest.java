package com.ynova.product.application.usecase;

import com.ynova.product.domain.model.Product;
import com.ynova.product.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

class CreateProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private CreateProductService createProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_ShouldSaveAndReturnProduct() {
        // Arrange
        Product product = Product.builder().name("Test").price(BigDecimal.valueOf(100.0)).build();
        Product savedProduct = Product.builder().id(1L).name("Test").price(BigDecimal.valueOf(100.0)).build();

        when(productRepositoryPort.save(product)).thenReturn(savedProduct);

        // Act
        Product result = createProductService.createProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepositoryPort).save(product);
    }
}

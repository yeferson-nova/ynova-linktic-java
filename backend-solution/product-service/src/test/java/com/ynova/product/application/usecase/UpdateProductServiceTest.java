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

class UpdateProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private UpdateProductService updateProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateProduct_ShouldUpdateAndReturnProduct() {
        // Arrange
        Long id = 1L;
        Product existingProduct = Product.builder().id(id).name("Old").price(BigDecimal.valueOf(50.0)).build();
        Product updateData = Product.builder().name("New").price(BigDecimal.valueOf(100.0)).build();
        Product updatedProduct = Product.builder().id(id).name("New").price(BigDecimal.valueOf(100.0)).build();

        when(productRepositoryPort.findById(id)).thenReturn(existingProduct);
        when(productRepositoryPort.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        Product result = updateProductService.updateProduct(id, updateData);

        // Assert
        assertNotNull(result);
        assertEquals("New", result.getName());
        assertEquals(BigDecimal.valueOf(100.0), result.getPrice());
        verify(productRepositoryPort).findById(id);
        verify(productRepositoryPort).save(any(Product.class));
    }
}

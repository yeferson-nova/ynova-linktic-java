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

class GetProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private GetProductService getProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProduct_ShouldReturnProduct_WhenExists() {
        // Arrange
        Long id = 1L;
        Product product = Product.builder().id(id).name("Test").build();
        when(productRepositoryPort.findById(id)).thenReturn(product);

        // Act
        Product result = getProductService.getProduct(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(productRepositoryPort).findById(id);
    }
}

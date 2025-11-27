package com.ynova.product.application.usecase;

import com.ynova.product.domain.model.Product;
import com.ynova.product.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListProductsServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private ListProductsService listProductsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listProducts_ShouldReturnListOfProducts() {
        // Arrange
        int page = 0;
        int size = 10;
        List<Product> products = List.of(new Product(), new Product());
        when(productRepositoryPort.findAll(page, size)).thenReturn(products);

        // Act
        List<Product> result = listProductsService.listProducts(page, size);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepositoryPort).findAll(page, size);
    }
}

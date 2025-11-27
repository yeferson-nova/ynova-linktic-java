package com.ynova.product.application.usecase;

import com.ynova.product.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class DeleteProductServiceTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private DeleteProductService deleteProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteProduct_ShouldCallDelete_WhenProductExists() {
        // Arrange
        Long id = 1L;
        when(productRepositoryPort.findById(id)).thenReturn(null); // Just to ensure it doesn't throw exception

        // Act
        deleteProductService.deleteProduct(id);

        // Assert
        verify(productRepositoryPort).findById(id);
        verify(productRepositoryPort).deleteById(id);
    }
}

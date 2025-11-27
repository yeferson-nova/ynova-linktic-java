package com.ynova.product.infrastructure.adapter.in.web;

import com.ynova.core.api.ApiResponse;
import com.ynova.product.domain.model.Product;
import com.ynova.product.domain.port.in.CreateProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

        private final CreateProductUseCase createProductUseCase;
        private final com.ynova.product.domain.port.in.GetProductUseCase getProductUseCase;
        private final com.ynova.product.domain.port.in.UpdateProductUseCase updateProductUseCase;
        private final com.ynova.product.domain.port.in.DeleteProductUseCase deleteProductUseCase;
        private final com.ynova.product.domain.port.in.ListProductsUseCase listProductsUseCase;

        @PostMapping
        public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody Product product) {
                Product createdProduct = createProductUseCase.createProduct(product);

                ApiResponse<Product> response = ApiResponse.<Product>builder()
                                .data(createdProduct)
                                .meta(Map.of("message", "Product created successfully"))
                                .build();

                return ResponseEntity.created(URI.create("/api/v1/products/" + createdProduct.getId()))
                                .body(response);
        }

        @org.springframework.web.bind.annotation.GetMapping("/{id}")
        public ResponseEntity<ApiResponse<Product>> getProduct(
                        @org.springframework.web.bind.annotation.PathVariable Long id) {
                Product product = getProductUseCase.getProduct(id);

                ApiResponse<Product> response = ApiResponse.<Product>builder()
                                .data(product)
                                .meta(Map.of("message", "Product retrieved successfully"))
                                .build();

                return ResponseEntity.ok(response);
        }

        @org.springframework.web.bind.annotation.PutMapping("/{id}")
        public ResponseEntity<ApiResponse<Product>> updateProduct(
                        @org.springframework.web.bind.annotation.PathVariable Long id,
                        @RequestBody Product product) {
                Product updatedProduct = updateProductUseCase.updateProduct(id, product);

                ApiResponse<Product> response = ApiResponse.<Product>builder()
                                .data(updatedProduct)
                                .meta(Map.of("message", "Product updated successfully"))
                                .build();

                return ResponseEntity.ok(response);
        }

        @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteProduct(
                        @org.springframework.web.bind.annotation.PathVariable Long id) {
                deleteProductUseCase.deleteProduct(id);

                ApiResponse<Void> response = ApiResponse.<Void>builder()
                                .meta(Map.of("message", "Product deleted successfully"))
                                .build();

                return ResponseEntity.ok(response);
        }

        @org.springframework.web.bind.annotation.GetMapping
        public ResponseEntity<ApiResponse<java.util.List<Product>>> listProducts(
                        @org.springframework.web.bind.annotation.RequestParam(defaultValue = "0") int page,
                        @org.springframework.web.bind.annotation.RequestParam(defaultValue = "10") int size) {
                java.util.List<Product> products = listProductsUseCase.listProducts(page, size);

                ApiResponse<java.util.List<Product>> response = ApiResponse.<java.util.List<Product>>builder()
                                .data(products)
                                .meta(Map.of("page", page, "size", size, "message", "Products retrieved successfully"))
                                .build();

                return ResponseEntity.ok(response);
        }
}

package com.ynova.inventory.infrastructure.adapter.out.client;

import com.ynova.core.api.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {

    private final RestClient.Builder restClientBuilder;

    @Value("${product.service.url:http://product-service:8080}")
    private String productServiceUrl;

    @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
    @Retry(name = "productService")
    public ProductDto getProduct(Long productId) {
        log.info("Calling Product Service for productId: {}", productId);

        ApiResponse<ProductDto> response = restClientBuilder.build()
                .get()
                .uri(productServiceUrl + "/api/v1/products/" + productId)
                .retrieve()
                .body(new ParameterizedTypeReference<ApiResponse<ProductDto>>() {
                });

        return response != null ? response.getData() : null;
    }

    public ProductDto getProductFallback(Long productId, Throwable t) {
        log.error("Failed to get product: {}, error: {}", productId, t.getMessage());

        return null;
    }
}

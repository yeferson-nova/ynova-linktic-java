package com.ynova.product.domain.port.in;

import com.ynova.product.domain.model.Product;
import java.util.List;

public interface ListProductsUseCase {
    List<Product> listProducts(int page, int size);
}

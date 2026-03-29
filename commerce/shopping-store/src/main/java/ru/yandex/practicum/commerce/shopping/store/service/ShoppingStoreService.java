package ru.yandex.practicum.commerce.shopping.store.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.dto.product.PageWithSort;
import ru.yandex.practicum.commerce.dto.product.ProductCategory;
import ru.yandex.practicum.commerce.dto.product.ProductDto;
import ru.yandex.practicum.commerce.dto.product.SetProductQuantityStateRequest;

import java.util.UUID;

public interface ShoppingStoreService {
    PageWithSort<ProductDto> getProducts(ProductCategory category, Pageable pageable);

    ProductDto putProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean removeProduct(UUID productId);

    boolean updateQuantity(SetProductQuantityStateRequest newState);

    ProductDto getProductById(UUID productId);
}

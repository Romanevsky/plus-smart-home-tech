package ru.yandex.practicum.commerce.shopping.store.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.shopping.store.ShoppingStoreOperations;
import ru.yandex.practicum.commerce.dto.product.*;
import ru.yandex.practicum.commerce.shopping.store.service.ShoppingStoreService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/shopping-store")
@Validated
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreOperations {
    private final ShoppingStoreService shoppingStoreService;

    @Override
    public PageWithSort<ProductDto> getProducts(ProductCategory category, Integer page, Integer size, List<String> sort) {
        Pageable pageParams = Pageable.unpaged();
        if (page != null && size != null) {
            pageParams = sort == null ? PageRequest.of(page, size)
                    : PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(sort.getLast()), sort.getFirst()));
        }
        return shoppingStoreService.getProducts(category, pageParams);
    }

    @Override
    public ProductDto putProduct(@Valid ProductDto productDto) {
        return shoppingStoreService.putProduct(productDto);
    }

    @Override
    public ProductDto updateProduct(@Valid ProductDto productDto) {
        return shoppingStoreService.updateProduct(productDto);
    }

    @Override
    public boolean removeProduct(@NotNull UUID productId) {
        return shoppingStoreService.removeProduct(productId);
    }

    @Override
    public boolean updateQuantity(@NotNull UUID productId, @NotNull QuantityState quantityState) {
        SetProductQuantityStateRequest quantityStateRequest = new SetProductQuantityStateRequest(productId, quantityState);
        return shoppingStoreService.updateQuantity(quantityStateRequest);
    }

    @Override
    public ProductDto getProductById(@NotNull UUID productId) {
        return shoppingStoreService.getProductById(productId);
    }
}

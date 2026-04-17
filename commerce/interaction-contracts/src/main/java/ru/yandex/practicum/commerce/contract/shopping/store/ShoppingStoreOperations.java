package ru.yandex.practicum.commerce.contract.shopping.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.product.ProductCategory;
import ru.yandex.practicum.commerce.dto.product.ProductDto;
import ru.yandex.practicum.commerce.dto.product.QuantityState;

import java.util.List;
import java.util.UUID;

public interface ShoppingStoreOperations {

    @GetMapping
    PagedModel<ProductDto> getProducts(@RequestParam ProductCategory category,
                                       @RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer size,
                                       @RequestParam(required = false) List<String> sort);

    @PutMapping
    ProductDto putProduct(@RequestBody @Valid ProductDto productDto);

    @PostMapping
    ProductDto updateProduct(@RequestBody @Valid ProductDto productDto);

    @PostMapping("/removeProductFromStore")
    boolean removeProduct(@RequestBody @NotNull UUID productId);

    @PostMapping("/quantityState")
    boolean updateQuantity(@RequestParam @NotNull UUID productId, @NotNull QuantityState quantityState);

    @GetMapping("/{productId}")
    ProductDto getProductById(@PathVariable @NotNull UUID productId);
}

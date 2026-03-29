package ru.yandex.practicum.commerce.contract.warehouse;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseOperations {

    @PutMapping
    void putNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request);

    @PostMapping("/check")
    BookedProductsDto checkShoppingCart(@RequestBody @Valid ShoppingCartDto shoppingCart);

    @PostMapping("/add")
    void addProductQuantity(@RequestBody @Valid AddProductToWarehouseRequest request);

    @GetMapping("/address")
    AddressDto getAddress();
}

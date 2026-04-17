package ru.yandex.practicum.commerce.warehouse.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseOperations;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.*;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseOperations {
    private final WarehouseService warehouseService;

    @Override
    public void putNewProduct(@Valid NewProductInWarehouseRequest request) {
        warehouseService.putNewProduct(request);
    }

    @Override
    public BookedProductsDto checkShoppingCart(@Valid ShoppingCartDto shoppingCart) {
        return warehouseService.checkShoppingCart(shoppingCart);
    }

    @Override
    public void addProductQuantity(@Valid AddProductToWarehouseRequest request) {
        warehouseService.addProductQuantity(request);
    }

    @Override
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }

    @Override
    public void returnProducts(@NotNull Map<@NotNull UUID, @NotNull @Positive Integer> products) {
        warehouseService.returnProducts(products);
    }

    @Override
    public BookedProductsDto assembleProducts(@Valid AssemblyProductsForOrderRequest request) {
        return warehouseService.assembleProducts(request);
    }
}

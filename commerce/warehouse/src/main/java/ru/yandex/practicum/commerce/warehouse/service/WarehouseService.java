package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.*;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {

    void putNewProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCart);

    void addProductQuantity(AddProductToWarehouseRequest request);

    void returnProducts(Map<UUID, Integer> products);

    BookedProductsDto assembleProducts(AssemblyProductsForOrderRequest request);

    AddressDto getAddress();
}

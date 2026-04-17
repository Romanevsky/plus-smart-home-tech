package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {

    void putNewProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCart);

    void addProductQuantity(AddProductToWarehouseRequest request);

    AddressDto getAddress();
}

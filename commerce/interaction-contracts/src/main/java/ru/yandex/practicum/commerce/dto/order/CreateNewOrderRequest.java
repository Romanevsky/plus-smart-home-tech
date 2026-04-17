package ru.yandex.practicum.commerce.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;

@Value
public class CreateNewOrderRequest {
    @NotNull
    @Valid
    ShoppingCartDto shoppingCart;

    @NotNull
    @Valid
    AddressDto deliveryAddress;
}

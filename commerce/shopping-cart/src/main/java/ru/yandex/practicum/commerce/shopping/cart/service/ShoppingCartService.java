package ru.yandex.practicum.commerce.shopping.cart.service;

import ru.yandex.practicum.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartDto getCart(String username);

    ShoppingCartDto putInCart(String username, Map<UUID, Integer> products);

    void deactivateCart(String username);

    ShoppingCartDto removeFromCart(String username, Collection<UUID> productIds);

    ShoppingCartDto changeQuantityInCart(String username, ChangeProductQuantityRequest request);
}

package ru.yandex.practicum.commerce.shopping.cart.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.shopping.cart.ShoppingCartOperations;
import ru.yandex.practicum.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.shopping.cart.service.ShoppingCartService;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/shopping-cart")
@Validated
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartOperations {
    private final ShoppingCartService shoppingCartService;

    @Override
    public ShoppingCartDto getCart(String username) {
        checkUsername(username);
        return shoppingCartService.getCart(username);
    }

    @Override
    public ShoppingCartDto putInCart(String username,
                                     @RequestBody @NotNull Map<@NotNull UUID, @NotNull @Positive Integer> products) {
        checkUsername(username);
        return shoppingCartService.putInCart(username, products);
    }

    @Override
    public void deactivateCart(String username) {
        checkUsername(username);
        shoppingCartService.deactivateCart(username);
    }

    @Override
    public ShoppingCartDto removeFromCart(String username,
                                          @NotNull @NotEmpty Collection<@NotNull UUID> productIds) {
        checkUsername(username);
        return shoppingCartService.removeFromCart(username, productIds);
    }

    @Override
    public ShoppingCartDto changeQuantityInCart(String username,
                                                @NotNull @Valid ChangeProductQuantityRequest request) {
        checkUsername(username);
        return shoppingCartService.changeQuantityInCart(username, request);
    }

    private void checkUsername(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Username can't be blank");
        }
    }
}

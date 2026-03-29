package ru.yandex.practicum.commerce.contract.shopping.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartOperations {

    @GetMapping
    ShoppingCartDto getCart(@RequestParam String username);

    @PutMapping
    ShoppingCartDto putInCart(@RequestParam String username,
                              @RequestBody @NotNull Map<@NotNull UUID, @NotNull @Positive Integer> products);

    @DeleteMapping
    void deactivateCart(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto removeFromCart(@RequestParam String username,
                                   @RequestBody @NotNull @NotEmpty Collection<@NotNull UUID> productIds);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantityInCart(@RequestParam String username,
                                         @RequestBody @NotNull @Valid ChangeProductQuantityRequest request);
}

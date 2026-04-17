package ru.yandex.practicum.commerce.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "shoppingCartId")
@ToString
public class ShoppingCartDto {
    @NotNull
    private UUID shoppingCartId;

    @NotNull
    private Map<@NotNull UUID, @NotNull @Positive Integer> products;
}

package ru.yandex.practicum.commerce.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.util.UUID;

@Value
public class ChangeProductQuantityRequest {
    @NotNull
    UUID productId;

    @Positive
    @NotNull
    Integer newQuantity;
}

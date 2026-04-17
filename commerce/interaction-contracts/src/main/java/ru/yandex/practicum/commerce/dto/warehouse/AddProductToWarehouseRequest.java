package ru.yandex.practicum.commerce.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.util.UUID;

@Value
public class AddProductToWarehouseRequest {
    @NotNull
    UUID productId;

    @Positive
    @NotNull
    Integer quantity;
}

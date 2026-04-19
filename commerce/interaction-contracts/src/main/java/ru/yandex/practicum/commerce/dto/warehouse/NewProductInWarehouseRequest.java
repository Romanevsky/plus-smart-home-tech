package ru.yandex.practicum.commerce.dto.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

@Value
public class NewProductInWarehouseRequest {
    @NotNull
    UUID productId;

    Boolean fragile;

    @NotNull
    @Valid
    DimensionDto dimension;

    @NotNull
    Double weight;
}

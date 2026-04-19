package ru.yandex.practicum.commerce.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
public class AssemblyProductsForOrderRequest {
    @NotNull
    Map<@NotNull UUID, @NotNull @Positive Integer> products;

    @NotNull
    UUID orderId;
}

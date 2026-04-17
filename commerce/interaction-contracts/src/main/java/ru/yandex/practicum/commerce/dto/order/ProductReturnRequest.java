package ru.yandex.practicum.commerce.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
public class ProductReturnRequest {
    @NotNull
    UUID orderId;

    @NotNull
    Map<@NotNull UUID, @NotNull @Positive Integer> products;
}

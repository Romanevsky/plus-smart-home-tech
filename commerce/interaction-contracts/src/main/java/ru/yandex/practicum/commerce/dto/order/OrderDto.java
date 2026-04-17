package ru.yandex.practicum.commerce.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "orderId")
@ToString
public class OrderDto {

    @NotNull
    private UUID orderId;

    private UUID shoppingCartId;

    @NotNull
    private Map<@NotNull UUID, @NotNull @Positive Integer> products;

    private UUID paymentId;

    private UUID deliveryId;

    private OrderState state;

    private Double deliveryWeight;

    private Double deliveryVolume;

    private Boolean fragile;

    private BigDecimal totalPrice;

    private BigDecimal deliveryPrice;

    private BigDecimal productPrice;
}

package ru.yandex.practicum.commerce.dto.delivery;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;

import java.util.UUID;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "deliveryId")
@ToString
public class DeliveryDto {

    private UUID deliveryId;

    @NotNull
    @Valid
    private AddressDto fromAddress;

    @NotNull
    @Valid
    private AddressDto toAddress;

    @NotNull
    private UUID orderId;

    private DeliveryState deliveryState;
}

package ru.yandex.practicum.commerce.delivery.service;

import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createDelivery(DeliveryDto delivery);

    void successfulDelivery(UUID orderId);

    void failedDelivery(UUID orderId);

    void pickedDelivery(UUID orderId);

    BigDecimal calculateDeliveryCost(OrderDto order);
}

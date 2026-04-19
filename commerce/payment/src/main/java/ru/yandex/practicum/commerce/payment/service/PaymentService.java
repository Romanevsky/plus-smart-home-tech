package ru.yandex.practicum.commerce.payment.service;

import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(OrderDto order);

    BigDecimal calculateTotalCost(OrderDto order);

    void paymentRefund(UUID paymentId);

    BigDecimal calculateProductCost(OrderDto order);

    void paymentFailed(UUID paymentId);
}

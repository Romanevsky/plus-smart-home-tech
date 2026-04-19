package ru.yandex.practicum.commerce.payment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.payment.PaymentOperations;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/payment")
@RequiredArgsConstructor
@Validated
public class PaymentController implements PaymentOperations {
    private PaymentService paymentService;

    @Override
    public PaymentDto createPayment(@NotNull @Valid OrderDto order) {
        return paymentService.createPayment(order);
    }

    @Override
    public BigDecimal calculateTotalCost(@NotNull @Valid OrderDto order) {
        return paymentService.calculateTotalCost(order);
    }

    @Override
    public void paymentRefund(@NotNull UUID paymentId) {
        paymentService.paymentRefund(paymentId);
    }

    @Override
    public BigDecimal calculateProductCost(@NotNull @Valid OrderDto order) {
        return paymentService.calculateProductCost(order);
    }

    @Override
    public void paymentFailed(@NotNull UUID paymentId) {
        paymentService.paymentFailed(paymentId);
    }
}

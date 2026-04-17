package ru.yandex.practicum.commerce.contract.payment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentOperations {

    @PostMapping
    PaymentDto createPayment(@RequestBody @NotNull @Valid OrderDto order);

    @PostMapping("/totalCost")
    BigDecimal calculateTotalCost(@RequestBody @NotNull @Valid OrderDto order);

    @PostMapping("/refund")
    void paymentRefund(@RequestBody @NotNull UUID paymentId);

    @PostMapping("/productCost")
    BigDecimal calculateProductCost(@RequestBody @NotNull @Valid OrderDto order);

    @PostMapping("/failed")
    void paymentFailed(@RequestBody @NotNull UUID paymentId);
}

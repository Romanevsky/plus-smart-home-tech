package ru.yandex.practicum.commerce.contract.payment;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient extends PaymentOperations {
}

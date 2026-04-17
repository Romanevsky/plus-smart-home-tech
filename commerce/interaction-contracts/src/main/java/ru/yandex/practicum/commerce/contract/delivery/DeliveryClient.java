package ru.yandex.practicum.commerce.contract.delivery;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient extends DeliveryOperations {
}

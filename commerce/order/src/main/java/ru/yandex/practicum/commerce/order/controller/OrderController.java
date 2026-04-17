package ru.yandex.practicum.commerce.order.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.order.OrderOperations;
import ru.yandex.practicum.commerce.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.exception.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/order")
@RequiredArgsConstructor
@Validated
public class OrderController implements OrderOperations {
    private final OrderService orderService;

    @Override
    public List<OrderDto> getOrders(@RequestParam String username) {
        return orderService.getOrders(username);
    }

    @Override
    public OrderDto putOrder(@RequestParam String username,
                             @RequestBody @NotNull @Valid CreateNewOrderRequest newOrderRequest) {
        checkUsername(username);
        return orderService.putOrder(username, newOrderRequest);
    }

    @Override
    public OrderDto returnOrder(@RequestBody @NotNull @Valid ProductReturnRequest productReturnRequest) {
        return orderService.returnOrder(productReturnRequest);
    }

    @Override
    public OrderDto payForOrder(@RequestBody @NotNull UUID orderId) {
        return orderService.payForOrder(orderId);
    }

    @Override
    public OrderDto payFailedForOrder(@RequestBody @NotNull UUID orderId) {
        return orderService.payFailedForOrder(orderId);
    }

    @Override
    public OrderDto deliverOrder(@RequestBody @NotNull UUID orderId) {
        return orderService.deliverOrder(orderId);
    }

    @Override
    public OrderDto deliveryFailedOrder(@RequestBody @NotNull UUID orderId) {
        return orderService.deliveryFailedOrder(orderId);
    }

    @Override
    public OrderDto completeOrder(@RequestBody @NotNull UUID orderId) {
        return orderService.completeOrder(orderId);
    }

    @Override
    public OrderDto calculateTotalForOrder(@RequestBody @NotNull UUID orderId) {
        return orderService.calculateTotalForOrder(orderId);
    }

    @Override
    public OrderDto calculateDeliveryForOrder(@RequestBody @NotNull UUID orderId) {
        return orderService.calculateDeliveryForOrder(orderId);
    }

    @Override
    public OrderDto assembleOrder(@RequestBody @NotNull UUID orderId) {
        return orderService.assembleOrder(orderId);
    }

    @Override
    public OrderDto assemblyFailedOrder(@RequestBody @NotNull UUID orderId) {
        return orderService.assemblyFailedOrder(orderId);
    }

    private void checkUsername(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Username can't be blank");
        }
    }
}

package ru.yandex.practicum.commerce.order.service;

import ru.yandex.practicum.commerce.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> getOrders(String username);

    OrderDto putOrder(String username,
                      CreateNewOrderRequest newOrderRequest);

    OrderDto returnOrder(ProductReturnRequest productReturnRequest);

    OrderDto payForOrder(UUID orderId);

    OrderDto payFailedForOrder(UUID orderId);

    OrderDto deliverOrder(UUID orderId);

    OrderDto deliveryFailedOrder(UUID orderId);

    OrderDto completeOrder(UUID orderId);

    OrderDto calculateTotalForOrder(UUID orderId);

    OrderDto calculateDeliveryForOrder(UUID orderId);

    OrderDto assembleOrder(UUID orderId);

    OrderDto assemblyFailedOrder(UUID orderId);
}

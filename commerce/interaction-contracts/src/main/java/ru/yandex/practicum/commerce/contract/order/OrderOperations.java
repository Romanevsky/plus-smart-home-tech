package ru.yandex.practicum.commerce.contract.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderOperations {

    @GetMapping
    List<OrderDto> getOrders(@RequestParam String username);

    @PutMapping
    OrderDto putOrder(@RequestParam String username,
                      @RequestBody @Valid CreateNewOrderRequest newOrderRequest);

    @PostMapping("/return")
    OrderDto returnOrder(@RequestBody @Valid ProductReturnRequest productReturnRequest);

    @PostMapping("/payment")
    OrderDto payForOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto payFailedForOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery")
    OrderDto deliverOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto deliveryFailedOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/completed")
    OrderDto completeOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/total")
    OrderDto calculateTotalForOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/delivery")
    OrderDto calculateDeliveryForOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/assembly")
    OrderDto assembleOrder(@RequestBody @NotNull UUID orderId);

    @PostMapping("/calculate/assembly/failed")
    OrderDto assemblyFailedOrder(@RequestBody @NotNull UUID orderId);
}

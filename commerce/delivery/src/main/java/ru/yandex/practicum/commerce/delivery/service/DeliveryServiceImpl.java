package ru.yandex.practicum.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.order.OrderClient;
import ru.yandex.practicum.commerce.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.commerce.delivery.model.AddressModel;
import ru.yandex.practicum.commerce.delivery.model.DeliveryModel;
import ru.yandex.practicum.commerce.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryState;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.exception.NoDeliveryFoundException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {
    private static final BigDecimal BASE_PRICE = BigDecimal.valueOf(5.0);
    private static final String WAREHOUSE_ONE = "ADDRESS_1";
    private static final String WAREHOUSE_TWO = "ADDRESS_2";
    private final DeliveryRepository deliveryRepository;
    private final OrderClient orderClient;
    private final DeliveryMapper mapper;

    @Override
    @Transactional
    public DeliveryDto createDelivery(DeliveryDto delivery) {
        delivery.setDeliveryState(DeliveryState.CREATED);
        return mapper.modelToDto(deliveryRepository.save(mapper.dtoToModel(delivery)));
    }

    @Override
    @Transactional
    public void successfulDelivery(UUID orderId) {
        DeliveryModel foundDelivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("No delivery found for order id %s".formatted(orderId)));
        foundDelivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(foundDelivery);
        orderClient.deliverOrder(orderId);
    }

    @Override
    @Transactional
    public void failedDelivery(UUID orderId) {
        DeliveryModel foundDelivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("No delivery found for order id %s".formatted(orderId)));
        foundDelivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(foundDelivery);
        orderClient.deliveryFailedOrder(orderId);
    }

    @Override
    @Transactional
    public void pickedDelivery(UUID orderId) {
        DeliveryModel foundDelivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("No delivery found for order id %s".formatted(orderId)));
        foundDelivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(foundDelivery);
    }

    @Override
    public BigDecimal calculateDeliveryCost(OrderDto order) {
        UUID orderId = order.getOrderId();
        DeliveryModel foundDelivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("No delivery found for order id %s".formatted(orderId)));
        AddressModel fromAddress = foundDelivery.getFromAddress();
        AddressModel toAddress = foundDelivery.getToAddress();
        BigDecimal totalCost = BASE_PRICE.multiply(BigDecimal.valueOf(fromAddress.getCountry().equals(WAREHOUSE_ONE) ? 1 : 2));
        totalCost = totalCost.multiply(BigDecimal.valueOf(order.getFragile() ? 1.2 : 1));
        totalCost = totalCost.add(BigDecimal.valueOf(order.getDeliveryWeight() * 0.3));
        totalCost = totalCost.add(BigDecimal.valueOf(order.getDeliveryVolume() * 0.2));
        totalCost = totalCost.multiply(BigDecimal.valueOf(fromAddress.getStreet().equals(toAddress.getStreet()) ? 1 : 1.2));
        return totalCost;
    }
}

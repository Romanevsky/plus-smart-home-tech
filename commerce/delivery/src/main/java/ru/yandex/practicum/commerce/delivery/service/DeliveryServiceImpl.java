package ru.yandex.practicum.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        log.info("Starting delivery cost calculation for order: {}", orderId);

        DeliveryModel foundDelivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> {
                    log.warn("No delivery found for order: {}", orderId);
                    return new NoDeliveryFoundException("No delivery found for order id %s".formatted(orderId));
                });

        AddressModel fromAddress = foundDelivery.getFromAddress();
        AddressModel toAddress = foundDelivery.getToAddress();

        log.debug("Order: {}, Fragile: {}, Weight: {}, Volume: {}, From: {} (Country: {}), To: {} (Street: {})",
                orderId, order.getFragile(), order.getDeliveryWeight(), order.getDeliveryVolume(),
                fromAddress.getStreet(), fromAddress.getCountry(), toAddress.getStreet(), toAddress.getStreet());

        BigDecimal totalCost = BASE_PRICE;
        log.debug("Base price applied: {}. Order: {}", totalCost, orderId);

        // Умножение на коэффициент страны отправления
        boolean isWarehouseOne = fromAddress.getCountry().equals(WAREHOUSE_ONE);
        totalCost = totalCost.multiply(BigDecimal.valueOf(isWarehouseOne ? 1 : 2));
        log.debug("After country factor (is WAREHOUSE_ONE: {}): {}. Order: {}", isWarehouseOne, totalCost, orderId);

        // Умножение на хрупкость
        if (order.getFragile()) {
            totalCost = totalCost.multiply(BigDecimal.valueOf(1.2));
            log.debug("Fragile goods surcharge applied (+20%). New total: {}. Order: {}", totalCost, orderId);
        } else {
            log.debug("No fragile surcharge. Total remains: {}. Order: {}", totalCost, orderId);
        }

        // Добавление стоимости веса
        BigDecimal weightCost = BigDecimal.valueOf(order.getDeliveryWeight() * 0.3);
        totalCost = totalCost.add(weightCost);
        log.debug("Weight cost added: {} ({} kg * 0.3). Total: {}. Order: {}", weightCost, order.getDeliveryWeight(), totalCost, orderId);

        // Добавление стоимости объёма
        BigDecimal volumeCost = BigDecimal.valueOf(order.getDeliveryVolume() * 0.2);
        totalCost = totalCost.add(volumeCost);
        log.debug("Volume cost added: {} ({} units * 0.2). Total: {}. Order: {}", volumeCost, order.getDeliveryVolume(), totalCost, orderId);

        // Коэффициент совпадения улиц
        boolean sameStreet = fromAddress.getStreet().equals(toAddress.getStreet());
        totalCost = totalCost.multiply(BigDecimal.valueOf(sameStreet ? 1 : 1.2));
        log.debug("Street match factor applied (same street: {}): {}. Final cost: {}. Order: {}",
                sameStreet, sameStreet ? "1.0" : "1.2", totalCost, orderId);

        log.info("Delivery cost calculation completed for order: {}. Final cost: {}", orderId, totalCost);
        return totalCost;
    }
}

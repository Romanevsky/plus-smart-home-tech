package ru.yandex.practicum.commerce.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.order.OrderClient;
import ru.yandex.practicum.commerce.contract.shopping.store.ShoppingStoreClient;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.exception.NoOrderFoundException;
import ru.yandex.practicum.commerce.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.commerce.payment.mapper.PaymentMapper;
import ru.yandex.practicum.commerce.payment.model.PaymentModel;
import ru.yandex.practicum.commerce.payment.model.PaymentStatus;
import ru.yandex.practicum.commerce.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;
    private final PaymentMapper mapper;

    @Override
    @Transactional
    public PaymentDto createPayment(OrderDto order) {
        BigDecimal productPrice = order.getProductPrice();
        BigDecimal deliveryPrice = order.getDeliveryPrice();
        BigDecimal totalPrice = order.getTotalPrice();
        if (productPrice == null || deliveryPrice == null || totalPrice == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Not enough info to calculate cost");
        }
        PaymentModel newPayment = PaymentModel.builder()
                .totalPayment(totalPrice)
                .orderId(order.getOrderId())
                .deliveryTotal(deliveryPrice)
                .feeTotal(totalPrice.subtract(productPrice).subtract(deliveryPrice))
                .status(PaymentStatus.PENDING)
                .build();
        return mapper.modelToDto(paymentRepository.save(newPayment));
    }

    @Override
    public BigDecimal calculateTotalCost(OrderDto order) {
        BigDecimal productPrice = order.getProductPrice();
        BigDecimal deliveryPrice = order.getDeliveryPrice();
        if (productPrice == null || deliveryPrice == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Not enough info to calculate cost");
        }
        BigDecimal tax = productPrice.multiply(BigDecimal.valueOf(0.1));
        return productPrice.add(tax).add(deliveryPrice);
    }

    @Override
    @Transactional
    public void paymentRefund(UUID paymentId) {
        PaymentModel payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoOrderFoundException("Payment with id %s not found".formatted(paymentId)));
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
        orderClient.payForOrder(payment.getOrderId());
    }

    @Override
    public BigDecimal calculateProductCost(OrderDto order) {
        Map<UUID, Integer> products = order.getProducts();
        return products.entrySet().stream()
                .map(entry ->
                        shoppingStoreClient.getProductById(entry.getKey()).getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add)
                .orElseThrow(() -> new NotEnoughInfoInOrderToCalculateException("Not enough info to calculate cost"));
    }

    @Override
    @Transactional
    public void paymentFailed(UUID paymentId) {
        PaymentModel payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoOrderFoundException("Payment with id %s not found".formatted(paymentId)));
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
        orderClient.payFailedForOrder(payment.getOrderId());
    }
}

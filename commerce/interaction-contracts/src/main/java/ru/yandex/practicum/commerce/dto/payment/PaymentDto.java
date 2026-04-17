package ru.yandex.practicum.commerce.dto.payment;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "paymentId")
@ToString
public class PaymentDto {

    private UUID paymentId;

    private BigDecimal totalPayment;

    private BigDecimal deliveryTotal;

    private BigDecimal feeTotal;
}

package ru.yandex.practicum.commerce.delivery.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryState;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "deliveries")
public class DeliveryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "delivery_id")
    private UUID deliveryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_address_id")
    private AddressModel fromAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_address_id")
    private AddressModel toAddress;

    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "delivery_state")
    @Enumerated(EnumType.STRING)
    private DeliveryState deliveryState;
}
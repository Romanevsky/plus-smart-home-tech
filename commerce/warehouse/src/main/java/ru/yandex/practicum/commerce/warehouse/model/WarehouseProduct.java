package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "warehouse_products")
public class WarehouseProduct {

    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column
    private Boolean fragile;

    @Embedded
    private Dimension dimension;

    @Column
    private Double weight;

    @Column
    private Long quantity = 0L;
}
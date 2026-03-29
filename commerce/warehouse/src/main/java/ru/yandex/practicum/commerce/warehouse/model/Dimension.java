package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Dimension {
    @Column
    private Double width;

    @Column
    private Double height;

    @Column
    private Double depth;

    public Double getVolume() {
        return width * height * depth;
    }
}

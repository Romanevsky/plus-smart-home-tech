package ru.yandex.practicum.commerce.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class DimensionDto {
    @NotNull
    private Double width;

    @NotNull
    private Double height;

    @NotNull
    private Double depth;
}

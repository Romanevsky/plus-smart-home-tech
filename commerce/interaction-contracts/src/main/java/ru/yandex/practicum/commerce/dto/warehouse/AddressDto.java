package ru.yandex.practicum.commerce.dto.warehouse;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class AddressDto {
    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
}

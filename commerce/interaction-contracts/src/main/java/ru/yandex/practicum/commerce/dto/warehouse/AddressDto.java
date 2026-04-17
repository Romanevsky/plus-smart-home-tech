package ru.yandex.practicum.commerce.dto.warehouse;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class AddressDto {
    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
}

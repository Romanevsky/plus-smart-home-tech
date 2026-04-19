package ru.yandex.practicum.commerce.order.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.order.model.AddressModel;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressModel dtoToModel(AddressDto dto);

    AddressDto modelToDto(AddressModel model);
}

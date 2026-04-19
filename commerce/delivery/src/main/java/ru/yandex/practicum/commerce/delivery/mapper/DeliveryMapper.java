package ru.yandex.practicum.commerce.delivery.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.delivery.model.DeliveryModel;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryDto modelToDto(DeliveryModel model);

    DeliveryModel dtoToModel(DeliveryDto dto);
}

package ru.yandex.practicum.commerce.order.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.order.model.OrderModel;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto modelToDto(OrderModel model);

    List<OrderDto> modelToDto(List<OrderModel> model);
}

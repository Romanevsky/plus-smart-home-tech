package ru.yandex.practicum.commerce.warehouse.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    WarehouseProduct dtoToModel(NewProductInWarehouseRequest dto);
}

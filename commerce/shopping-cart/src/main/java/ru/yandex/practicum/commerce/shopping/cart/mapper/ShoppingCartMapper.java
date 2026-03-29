package ru.yandex.practicum.commerce.shopping.cart.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCartModel;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    ShoppingCartDto dtoFromModel(ShoppingCartModel model);
}

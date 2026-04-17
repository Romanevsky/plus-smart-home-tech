package ru.yandex.practicum.commerce.shopping.store.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.commerce.dto.product.ProductDto;
import ru.yandex.practicum.commerce.shopping.store.model.ShoppingStoreProduct;

@Mapper(componentModel = "spring")
public interface ShoppingStoreProductMapper {
    ProductDto dtoFromModel(ShoppingStoreProduct model);

    ShoppingStoreProduct modelFromDto(ProductDto dto);
}

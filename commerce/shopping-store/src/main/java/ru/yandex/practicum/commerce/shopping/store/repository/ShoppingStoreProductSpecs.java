package ru.yandex.practicum.commerce.shopping.store.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.yandex.practicum.commerce.dto.product.ProductCategory;
import ru.yandex.practicum.commerce.shopping.store.model.ShoppingStoreProduct;

public class ShoppingStoreProductSpecs {
    public static Specification<ShoppingStoreProduct> categoryIs(ProductCategory category) {
        return (root, query, builder) ->
                builder.equal(root.get("productCategory"), category);
    }
}

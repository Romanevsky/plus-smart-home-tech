package ru.yandex.practicum.commerce.shopping.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.yandex.practicum.commerce.shopping.store.model.ShoppingStoreProduct;

import java.util.UUID;

public interface ShoppingStoreProductRepository extends JpaRepository<ShoppingStoreProduct, UUID>, JpaSpecificationExecutor<ShoppingStoreProduct> {
}

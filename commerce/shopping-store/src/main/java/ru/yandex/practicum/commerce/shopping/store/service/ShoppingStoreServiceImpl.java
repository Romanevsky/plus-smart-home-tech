package ru.yandex.practicum.commerce.shopping.store.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.product.*;
import ru.yandex.practicum.commerce.exception.ProductNotFoundException;
import ru.yandex.practicum.commerce.shopping.store.mapper.ShoppingStoreProductMapper;
import ru.yandex.practicum.commerce.shopping.store.model.ShoppingStoreProduct;
import ru.yandex.practicum.commerce.shopping.store.repository.ShoppingStoreProductRepository;
import ru.yandex.practicum.commerce.shopping.store.repository.ShoppingStoreProductSpecs;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingStoreServiceImpl implements ShoppingStoreService {
    private final ShoppingStoreProductRepository shoppingStoreProductRepository;
    private final ShoppingStoreProductMapper mapper;

    @Override
    public PageWithSort<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        Specification<ShoppingStoreProduct> spec = Specification.where(null);
        if (category != null) {
            spec = spec.and(ShoppingStoreProductSpecs.categoryIs(category));
        }

        Page<ShoppingStoreProduct> productsPage = shoppingStoreProductRepository.findAll(spec, pageable);
        return new PageWithSort<>(productsPage.map(mapper::dtoFromModel), pageable.getSort());
    }

    @Override
    @Transactional
    public ProductDto putProduct(ProductDto productDto) {
        productDto.setProductId(null);
        ShoppingStoreProduct savedProduct = shoppingStoreProductRepository.save(mapper.modelFromDto(productDto));
        return mapper.dtoFromModel(savedProduct);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        boolean exists = shoppingStoreProductRepository.existsById(productDto.getProductId());
        if (!exists) {
            throw new ProductNotFoundException("Product with id %s not found for update".formatted(productDto.getProductId()));
        }
        ShoppingStoreProduct updatedProduct = shoppingStoreProductRepository.save(mapper.modelFromDto(productDto));
        return mapper.dtoFromModel(updatedProduct);
    }

    @Transactional
    @Override
    public boolean removeProduct(UUID productId) {
        ShoppingStoreProduct productInDb = shoppingStoreProductRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id %s not found for removal".formatted(productId)));
        productInDb.setProductState(ProductState.DEACTIVATE);
        shoppingStoreProductRepository.save(productInDb);
        return true;
    }

    @Override
    @Transactional
    public boolean updateQuantity(SetProductQuantityStateRequest newState) {
        UUID uuid = newState.getProductId();
        ShoppingStoreProduct productInDb = shoppingStoreProductRepository.findById(uuid)
                .orElseThrow(() -> new ProductNotFoundException("Product with id %s not found for quantity update".formatted(uuid)));
        productInDb.setQuantityState(newState.getQuantityState());
        return true;
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        ShoppingStoreProduct productInDb = shoppingStoreProductRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id %s not found for quantity update".formatted(productId)));
        return mapper.dtoFromModel(productInDb);
    }
}

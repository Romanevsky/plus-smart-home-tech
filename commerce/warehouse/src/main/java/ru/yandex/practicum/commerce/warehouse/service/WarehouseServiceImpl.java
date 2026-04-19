package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.*;
import ru.yandex.practicum.commerce.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.commerce.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.warehouse.mapper.WarehouseMapper;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseRepository;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {
    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper mapper;

    @Override
    @Transactional
    public void putNewProduct(NewProductInWarehouseRequest request) {
        WarehouseProduct newProduct = mapper.dtoToModel(request);
        UUID id = newProduct.getProductId();
        if (warehouseRepository.existsById(id)) {
            throw new SpecifiedProductAlreadyInWarehouseException("Product with UUID %s already in warehouse".formatted(id));
        }
        warehouseRepository.save(newProduct);
    }

    @Override
    public BookedProductsDto checkShoppingCart(ShoppingCartDto shoppingCart) {
        Map<UUID, Integer> productsInCart = shoppingCart.getProducts();
        Set<UUID> productIds = productsInCart.keySet();
        List<WarehouseProduct> productsInWarehouse = warehouseRepository.findAllById(productIds);
        Map<UUID, Long> productQuantityMap = productsInWarehouse.stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, WarehouseProduct::getQuantity));

        productsInCart.forEach((key, value) -> productQuantityMap.merge(key, (long) -value,
                Long::sum));
        if (productQuantityMap.values().stream().anyMatch(value -> value < 0)) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException("Low product quantity in warehouse");
        }


        Double totalVolume = productsInWarehouse.stream().map(product ->
                        product.getDimension().getVolume() * productsInCart.get(product.getProductId())).reduce(Double::sum)
                .orElseThrow(() -> new IllegalStateException("Unable to calculate total volume for delivery"));
        Double totalWeight = productsInWarehouse.stream().map(product ->
                        product.getWeight() * productsInCart.get(product.getProductId())).reduce(Double::sum)
                .orElseThrow(() -> new IllegalStateException("Unable to calculate total weight for delivery"));
        Boolean fragile = productsInWarehouse.stream().anyMatch(WarehouseProduct::getFragile);
        return BookedProductsDto.builder()
                .deliveryVolume(totalVolume)
                .deliveryWeight(totalWeight)
                .fragile(fragile)
                .build();
    }

    @Override
    @Transactional
    public void addProductQuantity(AddProductToWarehouseRequest request) {
        UUID id = request.getProductId();
        WarehouseProduct productInDb = warehouseRepository.findById(id)
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Product with UUID %s not found warehouse".formatted(id)));
        productInDb.setQuantity(productInDb.getQuantity() + request.getQuantity());
        warehouseRepository.save(productInDb);
    }

    @Override
    @Transactional
    public void returnProducts(Map<UUID, Integer> products) {
        List<WarehouseProduct> productsInDb = warehouseRepository.findAllById(products.keySet());
        productsInDb.forEach(product -> {
            UUID id = product.getProductId();
            product.setQuantity(product.getQuantity() + products.get(id));
        });
        warehouseRepository.saveAll(productsInDb);
    }

    @Override
    @Transactional
    public BookedProductsDto assembleProducts(AssemblyProductsForOrderRequest request) {
        Map<UUID, Integer> productsInRequest = request.getProducts();
        List<WarehouseProduct> productsInWarehouse = warehouseRepository.findAllById(productsInRequest.keySet());
        productsInWarehouse.forEach(product -> {
            UUID id = product.getProductId();
            long newQuantity = product.getQuantity() - productsInRequest.get(id);
            if (newQuantity < 0) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException("Low product quantity in warehouse");
            }
            product.setQuantity(newQuantity);
        });

        Double totalVolume = productsInWarehouse.stream().map(product ->
                        product.getDimension().getVolume() * productsInRequest.get(product.getProductId())).reduce(Double::sum)
                .orElseThrow(() -> new IllegalStateException("Unable to calculate total volume for delivery"));
        Double totalWeight = productsInWarehouse.stream().map(product ->
                        product.getWeight() * productsInRequest.get(product.getProductId())).reduce(Double::sum)
                .orElseThrow(() -> new IllegalStateException("Unable to calculate total weight for delivery"));
        Boolean fragile = productsInWarehouse.stream().anyMatch(WarehouseProduct::getFragile);

        warehouseRepository.saveAll(productsInWarehouse);
        return BookedProductsDto.builder()
                .deliveryVolume(totalVolume)
                .deliveryWeight(totalWeight)
                .fragile(fragile)
                .build();
    }

    @Override
    public AddressDto getAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }
}

package ru.yandex.practicum.commerce.shopping.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseClient;
import ru.yandex.practicum.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.shopping.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.commerce.shopping.cart.model.ShoppingCartModel;
import ru.yandex.practicum.commerce.shopping.cart.repository.ShoppingCartRepository;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final WarehouseClient warehouseClient;
    private final ShoppingCartMapper mapper;


    @Override
    @Transactional
    public ShoppingCartDto getCart(String username) {
        ShoppingCartModel cartInDb = shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> shoppingCartRepository.save(createNewShoppingCart(username)));
        return mapper.dtoFromModel(cartInDb);
    }

    @Override
    @Transactional
    public ShoppingCartDto putInCart(String username, Map<UUID, Integer> products) {
        ShoppingCartModel cartInDb = shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> shoppingCartRepository.save(createNewShoppingCart(username)));
        if (!cartInDb.getIsActive()) {
            throw new IllegalStateException("Cart with UUID %s for user %s is in read-only mode, unable to add"
                    .formatted(cartInDb.getShoppingCartId(), username));
        }
        Map<UUID, Integer> productsInCart = cartInDb.getProducts();
        products.forEach((key, value) -> productsInCart.merge(key, value, Integer::sum));
        ShoppingCartDto dtoForCheck = mapper.dtoFromModel(cartInDb);
        warehouseClient.checkShoppingCart(dtoForCheck);

        cartInDb = shoppingCartRepository.save(cartInDb);
        return mapper.dtoFromModel(cartInDb);
    }

    @Override
    @Transactional
    public void deactivateCart(String username) {
        ShoppingCartModel cartInDb = shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> shoppingCartRepository.save(createNewShoppingCart(username)));
        cartInDb.setIsActive(false);
        shoppingCartRepository.save(cartInDb);
    }

    @Override
    @Transactional
    public ShoppingCartDto removeFromCart(String username, Collection<UUID> productIds) {
        ShoppingCartModel cartInDb = shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> shoppingCartRepository.save(createNewShoppingCart(username)));
        if (!cartInDb.getIsActive()) {
            throw new IllegalStateException("Cart with UUID %s for user %s is in read-only mode, unable to remove"
                    .formatted(cartInDb.getShoppingCartId(), username));
        }
        Map<UUID, Integer> productsInCart = cartInDb.getProducts();
        productIds.forEach((id) -> {
            Integer removed = productsInCart.remove(id);
            if (removed == null) {
                throw new NoProductsInShoppingCartException("No product with uuid %s in cart".formatted(id));
            }
        });
        cartInDb = shoppingCartRepository.save(cartInDb);
        return mapper.dtoFromModel(cartInDb);
    }

    @Override
    @Transactional
    public ShoppingCartDto changeQuantityInCart(String username, ChangeProductQuantityRequest request) {
        ShoppingCartModel cartInDb = shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> shoppingCartRepository.save(createNewShoppingCart(username)));
        if (!cartInDb.getIsActive()) {
            throw new IllegalStateException("Cart with UUID %s for user %s is in read-only mode, unable to remove"
                    .formatted(cartInDb.getShoppingCartId(), username));
        }
        Map<UUID, Integer> productsInCart = cartInDb.getProducts();
        UUID productId = request.getProductId();
        if (!productsInCart.containsKey(productId)) {
            throw new NoProductsInShoppingCartException("No product with uuid %s in cart".formatted(productId));
        }
        productsInCart.replace(productId, request.getNewQuantity());
        cartInDb = shoppingCartRepository.save(cartInDb);
        return mapper.dtoFromModel(cartInDb);
    }

    private ShoppingCartModel createNewShoppingCart(String username) {
        ShoppingCartModel newShoppingCart = new ShoppingCartModel();
        newShoppingCart.setUsername(username);
        return newShoppingCart;
    }
}

package org.demointernetshop.services;

import lombok.RequiredArgsConstructor;
import org.demointernetshop.dto.cart.CartChangeDto;
import org.demointernetshop.dto.cart.CartDto;
import org.demointernetshop.entity.Cart;
import org.demointernetshop.entity.CartItem;
import org.demointernetshop.entity.Product;
import org.demointernetshop.repository.CartRepository;
import org.demointernetshop.repository.ProductRepository;
import org.demointernetshop.services.utils.Converters;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final Converters converters;


    public CartDto getCart(Integer userId) {
        Cart cart = cartRepository.findByUserIdWithProducts(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user id: " + userId));

        return converters.convertToCartDto(cart);
    }

    public CartDto updateCart(Integer userId, CartChangeDto request) {
        Cart cart = cartRepository.findByUserIdWithProducts(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user id: " + userId));

        Product product = productRepository.findById(request.getProduct_id())
                .orElseThrow(() -> new RuntimeException("Product not found for product id: " + request.getProduct_id()));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);

                    cart.getCartItems().add(newItem);
                    return newItem;
                });
        cartItem.setQuantity(request.getCount());

        cartRepository.save(cart);

        return converters.convertToCartDto(cart);
    }
}

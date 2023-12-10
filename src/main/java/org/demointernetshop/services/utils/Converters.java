package org.demointernetshop.services.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.demointernetshop.dto.ManufacturerDto;
import org.demointernetshop.dto.cart.CartDto;
import org.demointernetshop.dto.category.CategoryDto;
import org.demointernetshop.dto.order.OrderDto;
import org.demointernetshop.dto.product.ProductDto;
import org.demointernetshop.dto.product.ProductShortInfoDto;
import org.demointernetshop.entity.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
//@NoArgsConstructor
public class Converters {
    public CategoryDto formCategoryToDto(Category category) {
        return new CategoryDto(category.getId(), category.getCategoryName());
    }

    public CartDto convertToCartDto(Cart cart) {
        List<ProductShortInfoDto> products = cart.getCartItems().stream()
                .map(this::convertToProductShortInfoDto)
                .toList();

        return new CartDto(cart.getId(), cart.getUser().getId(), products);
    }

    public ProductShortInfoDto convertToProductShortInfoDto(CartItem cartItem) {
        Product product = cartItem.getProduct();
        ProductInfo productInfo = product.getProductInfo();
        Integer quantity = cartItem.getQuantity();
        return new ProductShortInfoDto(product.getId(), product.getName(), productInfo.getPrice(), quantity);
    }

    public OrderDto convertToOrderDto(Order order, Integer userId, List<ProductShortInfoDto> productDtos) {
        return new OrderDto(
                order.getId(),
                userId,
                productDtos,
                order.getOrderStatus().getStatus(),
                order.getPaymentStatus().getStatus(),
                order.getPaymentMethod().getMethod(),
                order.getCreateData()
        );
    }

    public ProductDto convertToProductDto(
            Product product, Category category, Manufacturer manufacturer, ProductInfo productInfo) {
        CategoryDto categoryDto = CategoryDto.from(category);
        ManufacturerDto manufacturerDto = ManufacturerDto.from(manufacturer);

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                categoryDto,
                manufacturerDto,
                productInfo.getPrice(),
                productInfo.getQuantity());
    }
}

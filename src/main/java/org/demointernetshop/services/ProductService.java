package org.demointernetshop.services;

import lombok.RequiredArgsConstructor;
import org.demointernetshop.dto.product.ProductDto;
import org.demointernetshop.dto.product.ProductQuantityResponseDto;
import org.demointernetshop.entity.Product;
import org.demointernetshop.repository.ProductInfoRepository;
import org.demointernetshop.repository.ProductRepository;
import org.demointernetshop.services.utils.Converters;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ProductService {
    private final ProductRepository productRepository;
    private final ProductInfoRepository productInfoRepository;
    private final Converters converters;

    public List<ProductDto> getAllProducts() {
        return productRepository.findAllProductsWithInfo().stream()
                .map(product -> converters.convertToProductDto(product, product.getCategory(), product.getManufacturer(), product.getProductInfo()))
                .toList();
    }

    public List<ProductDto> getProductsByCategoryAndPrice(Integer categoryId, Double minPrice, Double maxPrice) {
        return productRepository.findProductsByCategoryAndPriceWithInfo(categoryId, minPrice, maxPrice).stream()
                .map(product -> converters.convertToProductDto(product, product.getCategory(), product.getManufacturer(), product.getProductInfo()))
                .toList();
    }

    public List<ProductDto> getProductsByCategoryAndManufacturerAndPrice(Integer categoryId, Integer manufacturerId, Double minPrice, Double maxPrice) {
        return productRepository.findProductsByCategoryAndManufacturerAndPriceWithInfo(categoryId, manufacturerId, minPrice, maxPrice).stream()
                .map(product -> converters.convertToProductDto(product, product.getCategory(), product.getManufacturer(), product.getProductInfo()))
                .toList();
    }

    public ProductDto getProductById(Integer productId) {
        Product product = productRepository.findByIdWithInfo(productId);
        return converters.convertToProductDto(
                product,
                product.getCategory(),
                product.getManufacturer(),
                product.getProductInfo());
    }

    public ProductQuantityResponseDto getProductQuantity(Integer productId) {
        Integer quantity = productInfoRepository.findByProduct_Id(productId)
                .orElseThrow(() -> new RuntimeException("Product Info not found for productId: " + productId))
                .getQuantity();

        return new ProductQuantityResponseDto(quantity);
    }
}

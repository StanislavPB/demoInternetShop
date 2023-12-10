package org.demointernetshop.repository;

import org.demointernetshop.dto.category.CategoryWithPricesDto;
import org.demointernetshop.entity.Product;
import org.demointernetshop.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductInfoRepository extends JpaRepository<ProductInfo, Integer> {
    Optional<ProductInfo> findByProduct_Id(Integer productId);

    @Query("SELECT new org.demointernetshop.dto.category.CategoryWithPricesDto(pi.product.category.id, MIN(pi.price), MAX(pi.price), pi.product.category.categoryName) " +
            "FROM ProductInfo pi " +
            "WHERE pi.product.category.id = :categoryId " +
            "GROUP BY pi.product.category.id, pi.product.category.categoryName")
    Optional<CategoryWithPricesDto> findPriceBoundsByCategory(Integer categoryId);

    @Query("SELECT new org.demointernetshop.dto.category.CategoryWithPricesDto(" +
            "pi.product.category.id, MIN(pi.price), MAX(pi.price), pi.product.category.categoryName) " +
            "FROM ProductInfo pi " +
            "GROUP BY pi.product.category.id, pi.product.category.categoryName")
    List<CategoryWithPricesDto> findPriceBoundsByAllCategories();
}

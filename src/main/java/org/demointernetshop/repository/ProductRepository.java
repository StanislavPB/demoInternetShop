package org.demointernetshop.repository;

import org.demointernetshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN FETCH p.productInfo pi " +
            "JOIN FETCH p.category c " +
            "JOIN FETCH p.manufacturer m")
    List<Product> findAllProductsWithInfo();

    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN FETCH p.productInfo pi " +
            "JOIN FETCH p.category c " +
            "JOIN FETCH p.manufacturer m " +
            "WHERE c.id = :categoryId " +
            "AND pi.price >= :minPrice " +
            "AND pi.price <= :maxPrice")
    List<Product> findProductsByCategoryAndPriceWithInfo(Integer categoryId, Double minPrice, Double maxPrice);

    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN FETCH p.productInfo pi " +
            "JOIN FETCH p.category c " +
            "JOIN FETCH p.manufacturer m " +
            "WHERE c.id = :categoryId " +
            "AND m.id = :manufacturerId " +
            "AND pi.price >= :minPrice " +
            "AND pi.price <= :maxPrice")
    List<Product>  findProductsByCategoryAndManufacturerAndPriceWithInfo(Integer categoryId, Integer manufacturerId, Double minPrice, Double maxPrice);

    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN FETCH p.productInfo pi " +
            "JOIN FETCH p.category c " +
            "JOIN FETCH p.manufacturer m " +
            "WHERE p.id = :productId ")
    Product findByIdWithInfo(Integer productId);
}

package org.demointernetshop.repository;

import org.demointernetshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("SELECT c FROM Cart c " +
            "LEFT JOIN FETCH c.cartItems ci " +
            "LEFT JOIN FETCH ci.product p " +
            "LEFT JOIN FETCH p.productInfo " +
            "WHERE c.user.id = :userId")
    Optional<Cart> findByUserIdWithProducts(Integer userId);
}

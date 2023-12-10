package org.demointernetshop.repository;

import org.demointernetshop.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Integer> {
    Optional<PaymentStatus> findByStatus(String status);
}

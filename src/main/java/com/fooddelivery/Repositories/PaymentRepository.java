package com.fooddelivery.Repositories;

import com.fooddelivery.Entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT p FROM Payment p WHERE p.id = :id AND p.isActive = true")
    Optional<Payment> findActiveById(@Param("id") Integer id);

    @Query("SELECT p FROM Payment p WHERE p.isActive = true")
    List<Payment> findAllActive();

    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId AND p.isActive = true")
    Optional<Payment> findByOrderId(@Param("orderId") Integer orderId);

    @Query("SELECT p FROM Payment p WHERE p.isActive = true AND " +
            "(:method IS NULL OR p.paymentMethod = :method) AND " +
            "(:status IS NULL OR p.status = :status) AND " +
            "(:fromDate IS NULL OR p.processedAt >= :fromDate) AND " +
            "(:toDate IS NULL OR p.processedAt <= :toDate)")
    Page<Payment> findPaymentsWithFilters(
            @Param("method") String method,
            @Param("status") String status,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );

    @Query("SELECT p.paymentMethod, SUM(p.amount) FROM Payment p WHERE p.isActive = true GROUP BY p.paymentMethod")
    List<Object[]> getPaymentAnalyticsByMethod();
}
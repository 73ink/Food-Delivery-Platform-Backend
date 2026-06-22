package com.fooddelivery.Repositories;

import com.fooddelivery.Entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE c.id = :id AND c.isActive = true")
    Optional<Customer> findActiveById(@Param("id") Integer id);

    @Query("SELECT c FROM Customer c WHERE c.isActive = true")
    List<Customer> findAllActive();

    @Query("SELECT c FROM Customer c WHERE c.email = :email AND c.isActive = true")
    Optional<Customer> findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Customer c WHERE c.loyaltyPoints >= :points AND c.isActive = true")
    List<Customer> findByLoyaltyPointsGreaterThanEqual(@Param("points") int points);

    @Query("SELECT c FROM Customer c WHERE c.createdDate BETWEEN :startDate AND :endDate AND c.isActive = true")
    List<Customer> findCustomersRegisteredBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT c FROM Customer c WHERE c.isActive = true AND " +
            "(LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
            "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Customer> searchByName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT * FROM customers WHERE is_active = true ORDER BY loyalty_points DESC LIMIT 10",
            nativeQuery = true)
    List<Customer> findTop10ByLoyaltyPoints();
}
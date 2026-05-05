package com.flashsale.flashsale.sale.repository;

import com.flashsale.flashsale.sale.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = """
        SELECT COUNT(*)
        FROM orders
        WHERE user_id = :userId
        AND DATE(created_at) = CURRENT_DATE
        """, nativeQuery = true)
    int countTodayOrder(Long userId);
}
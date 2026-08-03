package com.bookstore.tss_assignment_online_bookstore_management_system.repository;

import com.bookstore.tss_assignment_online_bookstore_management_system.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderOrderId(Long orderId);
}

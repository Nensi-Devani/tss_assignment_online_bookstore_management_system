package com.bookstore.tss_assignment_online_bookstore_management_system.service;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.order.OrderRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.order.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto placeOrder(OrderRequestDto requestDto);

    OrderResponseDto getById(Long orderId);

    Page<OrderResponseDto> getAll(Pageable pageable);

    Page<OrderResponseDto> getOrdersByUser(Long userId, Pageable pageable);

    OrderResponseDto cancelOrder(Long orderId);
}

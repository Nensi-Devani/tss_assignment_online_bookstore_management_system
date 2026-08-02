package com.bookstore.tss_assignment_online_bookstore_management_system.dto.order;

import com.bookstore.tss_assignment_online_bookstore_management_system.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {

    private Long orderId;

    private Long userId;

    private String userName;

    private LocalDateTime orderDate;

    private Double totalAmount;

    private OrderStatus orderStatus;

    private List<OrderItemResponseDto> orderItems;

}

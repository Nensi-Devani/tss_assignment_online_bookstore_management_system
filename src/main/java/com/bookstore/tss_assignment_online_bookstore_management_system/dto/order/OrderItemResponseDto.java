package com.bookstore.tss_assignment_online_bookstore_management_system.dto.order;

import lombok.Data;

@Data
public class OrderItemResponseDto {

    private Long orderItemId;

    private Long bookId;

    private String bookTitle;

    private Integer quantity;

    private Double priceAtPurchase;

}

package com.bookstore.tss_assignment_online_bookstore_management_system.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {

    @NotNull(message = "User ID is required.")
    private Long userId;

    @NotEmpty(message = "Order must contain at least one book.")
    @Valid
    private List<OrderItemRequestDto> orderItems;

}

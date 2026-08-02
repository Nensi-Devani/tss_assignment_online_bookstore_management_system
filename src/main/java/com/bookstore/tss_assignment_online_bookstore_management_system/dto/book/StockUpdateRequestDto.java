package com.bookstore.tss_assignment_online_bookstore_management_system.dto.book;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class StockUpdateRequestDto {

    @NotNull(message = "Stock is required.")
    @PositiveOrZero(message = "Stock cannot be negative.")
    private Integer stock;

}

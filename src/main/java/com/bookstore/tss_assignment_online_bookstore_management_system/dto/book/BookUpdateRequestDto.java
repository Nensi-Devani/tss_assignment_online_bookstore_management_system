package com.bookstore.tss_assignment_online_bookstore_management_system.dto.book;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class BookUpdateRequestDto {

    @NotBlank(message = "Title is required.")
    private String title;

    private String description;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    private Double price;

    @NotNull(message = "Stock is required.")
    @Min(value = 0, message = "Stock cannot be negative.")
    private Integer stock;

    @NotEmpty(message = "At least one author is required.")
    private Set<Long> authorIds;
}

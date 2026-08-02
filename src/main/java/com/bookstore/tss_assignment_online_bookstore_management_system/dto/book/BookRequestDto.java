package com.bookstore.tss_assignment_online_bookstore_management_system.dto.book;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class BookRequestDto {

    @NotBlank(message = "Book title is required.")
    @Size(min = 2, max = 200, message = "Book title must be between 2 and 200 characters.")
    private String title;

    @NotBlank(message = "ISBN is required.")
    @Size(min = 10, max = 20, message = "ISBN must be between 10 and 20 characters.")
    private String isbn;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters.")
    private String description;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0.")
    private Double price;

    @NotNull(message = "Stock is required.")
    @PositiveOrZero(message = "Stock cannot be negative.")
    private Integer stock;

    @NotNull(message = "Category is required.")
    private Long categoryId;

    @NotNull(message = "Publisher is required.")
    private Long publisherId;

    @NotEmpty(message = "At least one author is required.")
    private Set<Long> authorIds;

}

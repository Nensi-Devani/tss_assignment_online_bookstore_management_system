package com.bookstore.tss_assignment_online_bookstore_management_system.dto.review;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequestDto {

    @NotNull(message = "User ID is required.")
    private Long userId;

    @NotNull(message = "Book ID is required.")
    private Long bookId;

    @NotNull(message = "Rating is required.")
    @Min(value = 1, message = "Rating must be at least 1.")
    @Max(value = 5, message = "Rating cannot be greater than 5.")
    private Integer rating;

    @NotBlank(message = "Comment is required.")
    @Size(max = 2000, message = "Comment cannot exceed 2000 characters.")
    private String comment;

}

package com.bookstore.tss_assignment_online_bookstore_management_system.dto.review;

import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import lombok.Data;

@Data
public class ReviewResponseDto {

    private Long reviewId;

    private Long userId;

    private String userName;

    private Long bookId;

    private String bookTitle;

    private Integer rating;

    private String comment;dd
}

package com.bookstore.tss_assignment_online_bookstore_management_system.dto.book;

import lombok.Data;

@Data
public class BookSearchRequestDto {

    private String title;

    private Long categoryId;

    private Long authorId;

    private Double price;

    private Boolean inStock;

}

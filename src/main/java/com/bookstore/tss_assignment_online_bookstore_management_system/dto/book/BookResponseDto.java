package com.bookstore.tss_assignment_online_bookstore_management_system.dto.book;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.author.AuthorResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.category.CategoryResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher.PublisherResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import lombok.Data;

import java.util.Set;

@Data
public class BookResponseDto {

    private Long bookId;

    private String title;

    private String isbn;

    private String description;

    private Double price;

    private Integer stock;

    private String coverImagePath;

    private CategoryResponseDto category;

    private PublisherResponseDto publisher;

    private Set<AuthorResponseDto> authors;

    private Double averageRating;

    private Status status;

}

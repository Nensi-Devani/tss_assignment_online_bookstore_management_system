package com.bookstore.tss_assignment_online_bookstore_management_system.service;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BookResponseDto create(BookRequestDto requestDto);

    BookResponseDto getById(Long bookId);

    Page<BookResponseDto> getAll(Pageable pageable);

    BookResponseDto update(Long bookId, BookUpdateRequestDto requestDto);

    void delete(Long bookId);

    Page<BookResponseDto> search(
            String title,
            Long categoryId,
            Long authorId,
            Double price,
            Boolean inStock,
            Pageable pageable
    );

    List<BookResponseDto> getBooksByAuthor(Long authorId);

    BookResponseDto adjustStock(Long bookId, Integer stock);

    Double getAverageRating(Long bookId);
}

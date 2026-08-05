package com.bookstore.tss_assignment_online_bookstore_management_system.controller;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookUpdateRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponseDto> create(@Valid @RequestBody BookRequestDto requestDto) {
        return new ResponseEntity<>(bookService.create(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDto> getById(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getById(bookId));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<BookResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(bookService.getAll(pageable));
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookResponseDto> update(@PathVariable Long bookId, @Valid @RequestBody BookUpdateRequestDto requestDto) {
        return ResponseEntity.ok(bookService.update(bookId, requestDto));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> delete(@PathVariable Long bookId) {
        bookService.delete(bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<BookResponseDto>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Boolean inStock,
            Pageable pageable) {

        return ResponseEntity.ok(
                bookService.search(
                        title,
                        categoryId,
                        authorId,
                        price,
                        inStock,
                        pageable
                )
        );
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BookResponseDto>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    @PatchMapping("/{bookId}/stock")
    public ResponseEntity<BookResponseDto> updateStock(@PathVariable Long bookId, @RequestParam Integer stock) {
        return ResponseEntity.ok(bookService.adjustStock(bookId, stock));
    }

    @GetMapping("/{bookId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getAverageRating(bookId));
    }
}

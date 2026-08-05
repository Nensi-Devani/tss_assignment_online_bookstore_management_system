package com.bookstore.tss_assignment_online_bookstore_management_system.controller;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.review.ReviewRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.review.ReviewResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.ReviewService;
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
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> create(@Valid @RequestBody ReviewRequestDto requestDto) {
        return new ResponseEntity<>(reviewService.create(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getById(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getById(reviewId));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ReviewResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(reviewService.getAll(pageable));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBook(bookId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(userId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> update(@PathVariable Long reviewId, @Valid @RequestBody ReviewRequestDto requestDto) {
        return ResponseEntity.ok(reviewService.update(reviewId, requestDto));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(@PathVariable Long reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.noContent().build();
    }
}

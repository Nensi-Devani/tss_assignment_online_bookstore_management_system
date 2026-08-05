package com.bookstore.tss_assignment_online_bookstore_management_system.service;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.review.ReviewRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.review.ReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {

    ReviewResponseDto create(ReviewRequestDto requestDto);

    ReviewResponseDto getById(Long reviewId);

    PageResponseDto<ReviewResponseDto> getAll(Pageable pageable);

    List<ReviewResponseDto> getReviewsByBook(Long bookId);

    List<ReviewResponseDto> getReviewsByUser(Long userId);

    ReviewResponseDto update(Long reviewId, ReviewRequestDto requestDto);

    void delete(Long reviewId);
}

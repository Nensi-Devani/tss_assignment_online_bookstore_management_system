package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.review.ReviewRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.review.ReviewResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Book;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Review;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.User;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.ReviewMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.BookRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.ReviewRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.UserRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewResponseDto create(ReviewRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));

        Review review = reviewMapper.toEntity(requestDto);

        review.setUser(user);
        review.setBook(book);

        return reviewMapper.toResponseDto(reviewRepository.save(review));
    }

    @Override
    public ReviewResponseDto getById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found."));

        return reviewMapper.toResponseDto(review);
    }

    @Override
    public Page<ReviewResponseDto> getAll(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(reviewMapper::toResponseDto);
    }

    @Override
    public List<ReviewResponseDto> getReviewsByBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found.");
        }

        return reviewRepository.findByBookBookId(bookId)
                .stream()
                .map(reviewMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<ReviewResponseDto> getReviewsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found.");
        }

        return reviewRepository.findByUserUserId(userId)
                .stream()
                .map(reviewMapper::toResponseDto)
                .toList();
    }

    @Override
    public ReviewResponseDto update(Long reviewId, ReviewRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found."));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));

        reviewMapper.updateEntity(requestDto, review);

        review.setUser(user);
        review.setBook(book);

        return reviewMapper.toResponseDto(reviewRepository.save(review));
    }

    @Override
    public void delete(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found."));

        reviewRepository.delete(review);
    }
}

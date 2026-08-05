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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewResponseDto create(ReviewRequestDto requestDto) {
        logger.info("Creating review. UserId={}, BookId={}", requestDto.getUserId(), requestDto.getBookId());

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> {
                    logger.warn("User not found. UserId={}", requestDto.getUserId());
                    return new ResourceNotFoundException("User not found.");
                });

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> {
                    logger.warn("Book not found. BookId={}", requestDto.getBookId());
                    return new ResourceNotFoundException("Book not found.");
                });

        Review review = reviewMapper.toEntity(requestDto);

        review.setUser(user);
        review.setBook(book);

        Review savedReview = reviewRepository.save(review);

        logger.info("Review created successfully. ReviewId={}", savedReview.getReviewId());

        return reviewMapper.toResponseDto(savedReview);
    }

    @Override
    public ReviewResponseDto getById(Long reviewId) {
        logger.info("Fetching review. ReviewId={}", reviewId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    logger.warn("Review not found. ReviewId={}", reviewId);
                    return new ResourceNotFoundException("Review not found.");
                });

        logger.info("Review fetched successfully. ReviewId={}", reviewId);

        return reviewMapper.toResponseDto(review);
    }

    @Override
    public Page<ReviewResponseDto> getAll(Pageable pageable) {
        logger.info("Fetching all reviews. Page={}, Size={}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        Page<ReviewResponseDto> reviews =  reviewRepository.findAll(pageable)
                .map(reviewMapper::toResponseDto);

        logger.info("Retrieved {} reviews.", reviews.getNumberOfElements());

        return reviews;
    }

    @Override
    public List<ReviewResponseDto> getReviewsByBook(Long bookId) {
        logger.info("Fetching reviews for BookId={}", bookId);

        if (!bookRepository.existsById(bookId)) {
            logger.warn("Book not found. BookId={}", bookId);
            throw new ResourceNotFoundException("Book not found.");
        }

        List<ReviewResponseDto> reviews = reviewRepository.findByBookBookId(bookId)
                .stream()
                .map(reviewMapper::toResponseDto)
                .toList();

        logger.info("Retrieved {} reviews for BookId={}", reviews.size(), bookId);

        return reviews;
    }

    @Override
    public List<ReviewResponseDto> getReviewsByUser(Long userId) {
        logger.info("Fetching reviews for UserId={}", userId);

        if (!userRepository.existsById(userId)) {
            logger.warn("User not found. UserId={}", userId);
            throw new ResourceNotFoundException("User not found.");
        }

        List<ReviewResponseDto> reviews = reviewRepository.findByUserUserId(userId)
                .stream()
                .map(reviewMapper::toResponseDto)
                .toList();

        logger.info("Retrieved {} reviews for UserId={}", reviews.size(), userId);

        return reviews;
    }

    @Override
    public ReviewResponseDto update(Long reviewId, ReviewRequestDto requestDto) {
        logger.info("Updating review. ReviewId={}", reviewId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    logger.warn("Review not found. ReviewId={}", reviewId);
                    return new ResourceNotFoundException("Review not found.");
                });

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> {
                    logger.warn("User not found. UserId={}", requestDto.getUserId());
                    return new ResourceNotFoundException("User not found.");
                });

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> {
                    logger.warn("Book not found. BookId={}", requestDto.getBookId());
                    return new ResourceNotFoundException("Book not found.");
                });

        reviewMapper.updateEntity(requestDto, review);

        review.setUser(user);
        review.setBook(book);

        Review updatedReview = reviewRepository.save(review);

        logger.info("Review updated successfully. ReviewId={}", updatedReview.getReviewId());

        return reviewMapper.toResponseDto(updatedReview);
    }

    @Override
    public void delete(Long reviewId) {
        logger.info("Deleting review. ReviewId={}", reviewId);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    logger.warn("Review not found. ReviewId={}", reviewId);
                    return new ResourceNotFoundException("Review not found.");
                });

        reviewRepository.delete(review);

        logger.info("Review deleted successfully. ReviewId={}", reviewId);
    }
}

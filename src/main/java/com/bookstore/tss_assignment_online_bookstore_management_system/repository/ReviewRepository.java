package com.bookstore.tss_assignment_online_bookstore_management_system.repository;

import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBookBookId(Long bookId);

    List<Review> findByUserUserId(Long userId);

    @Query("""
            SELECT AVG(r.rating)
            FROM Review r
            WHERE r.book.bookId = :bookId
            """)
    Double getAverageRating(Long bookId);
}

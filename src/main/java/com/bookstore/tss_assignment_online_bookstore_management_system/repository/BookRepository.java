package com.bookstore.tss_assignment_online_bookstore_management_system.repository;

import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Book> findByAuthorsAuthorId(Long authorId);
}

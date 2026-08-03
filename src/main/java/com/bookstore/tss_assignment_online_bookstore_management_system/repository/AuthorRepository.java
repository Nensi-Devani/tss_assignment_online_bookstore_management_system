package com.bookstore.tss_assignment_online_bookstore_management_system.repository;

import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Author;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    boolean existsByName(String name);

    List<Author> findByStatus(Status status);

    boolean existsById(Long authorId);
}

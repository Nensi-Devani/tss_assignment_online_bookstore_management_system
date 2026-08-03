package com.bookstore.tss_assignment_online_bookstore_management_system.specification;

import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Author;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Book;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Category;
import jakarta.persistence.criteria.Join;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor
public class BookSpecification {

    public static Specification<Book> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Book> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }

            Join<Book, Category> categoryJoin = root.join("category");

            return criteriaBuilder.equal(
                    categoryJoin.get("categoryId"),
                    categoryId
            );
        };
    }

    public static Specification<Book> hasAuthor(Long authorId) {
        return (root, query, criteriaBuilder) -> {
            if (authorId == null) {
                return criteriaBuilder.conjunction();
            }

            query.distinct(true);

            Join<Book, Author> authorJoin = root.join("authors");

            return criteriaBuilder.equal(
                    authorJoin.get("authorId"),
                    authorId
            );
        };
    }

    public static Specification<Book> hasMinPrice(Double minPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get("price"),
                    minPrice
            );
        };
    }

    public static Specification<Book> hasMaxPrice(Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    root.get("price"),
                    maxPrice
            );
        };
    }

    public static Specification<Book> isInStock(Boolean inStock) {
        return (root, query, criteriaBuilder) -> {
            if (inStock == null) {
                return criteriaBuilder.conjunction();
            }

            if (inStock) {
                return criteriaBuilder.greaterThan(root.get("stock"), 0);
            }

            return criteriaBuilder.equal(root.get("stock"), 0);
        };
    }

}

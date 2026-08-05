package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookUpdateRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Author;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Book;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Category;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Publisher;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.DuplicateResourceException;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.BookMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.*;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.BookService;
import com.bookstore.tss_assignment_online_bookstore_management_system.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final ReviewRepository reviewRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponseDto create(BookRequestDto requestDto) {
        logger.info("Creating book. Title: {}, ISBN: {}", requestDto.getTitle(), requestDto.getIsbn());

        if (bookRepository.existsByIsbn(requestDto.getIsbn())) {
            logger.warn("Book creation failed. ISBN already exists: {}", requestDto.getIsbn());
            throw new DuplicateResourceException("ISBN already exists.");
        }

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> {
                    logger.warn("Book creation failed. Category not found. CategoryId={}", requestDto.getCategoryId());
                    return new ResourceNotFoundException("Category not found.");
                });

        Publisher publisher = publisherRepository.findById(requestDto.getPublisherId())
                .orElseThrow(() -> {
                    logger.warn("Book creation failed. Publisher not found. PublisherId={}", requestDto.getPublisherId());
                    return new ResourceNotFoundException("Publisher not found.");
                });

        Set<Author> authors = new HashSet<>(
                authorRepository.findAllById(requestDto.getAuthorIds())
        );

        if (authors.size() != requestDto.getAuthorIds().size()) {
            logger.warn("Book creation failed. One or more authors not found.");
            throw new ResourceNotFoundException("One or more authors not found.");
        }

        Book book = bookMapper.toEntity(requestDto);

        book.setCategory(category);
        book.setPublisher(publisher);
        book.setAuthors(authors);

        Book savedBook = bookRepository.save(book);
        logger.info("Book created successfully. BookId={}", savedBook.getBookId());

        return bookMapper.toResponseDto(savedBook);
    }

    @Override
    public BookResponseDto getById(Long bookId) {
        logger.info("Fetching book with ID: {}", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.warn("Book not found with ID: {}", bookId);
                    return new ResourceNotFoundException("Book not found.");
                });

        logger.info("Book fetched successfully with ID: {}", bookId);

        return bookMapper.toResponseDto(book);
    }

    @Override
    public Page<BookResponseDto> getAll(Pageable pageable) {
        logger.info("Fetching all books. Page: {}, Size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        Page<BookResponseDto> books = bookRepository.findAll(pageable)
                .map(bookMapper::toResponseDto);

        logger.info("Fetched {} books.", books.getNumberOfElements());

        return books;
    }

    @Override
    public BookResponseDto update(Long bookId, BookUpdateRequestDto requestDto) {
        logger.info("Updating book with ID: {}", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.warn("Book not found with ID: {}", bookId);
                    return new ResourceNotFoundException("Book not found.");
                });

        Set<Author> authors = new HashSet<>(
                authorRepository.findAllById(requestDto.getAuthorIds())
        );

        if (authors.size() != requestDto.getAuthorIds().size()) {
            logger.warn("One or more authors not found while updating book.");
            throw new ResourceNotFoundException("One or more authors not found.");
        }

        bookMapper.updateEntity(requestDto, book);

        book.setAuthors(authors);

        Book updatedBook = bookRepository.save(book);

        logger.info("Book updated successfully with ID: {}", updatedBook.getBookId());

        return bookMapper.toResponseDto(updatedBook);
    }

    @Override
    public void delete(Long bookId) {
        logger.info("Deleting book with ID: {}", bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.warn("Book not found with ID: {}", bookId);
                    return new ResourceNotFoundException("Book not found.");
                });

        book.setStatus(Status.DELETED);

        bookRepository.save(book);

        logger.info("Book marked as DELETED with ID: {}", bookId);
    }

    @Override
    public Page<BookResponseDto> search(String title, Long categoryId, Long authorId, Double price, Boolean inStock, Pageable pageable) {
        logger.info(
                "Searching books. Title={}, CategoryId={}, AuthorId={}, Price={}, InStock={}, Page={}, Size={}",
                title,
                categoryId,
                authorId,
                price,
                inStock,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Specification<Book> specification = Specification
                .where(BookSpecification.hasTitle(title))
                .and(BookSpecification.hasCategory(categoryId))
                .and(BookSpecification.hasAuthor(authorId))
                .and(BookSpecification.hasPrice(price))
                .and(BookSpecification.isInStock(inStock));

        Page<BookResponseDto> books =  bookRepository.findAll(specification, pageable)
                .map(bookMapper::toResponseDto);

        logger.info("Search completed successfully. {} books found.", books.getTotalElements());

        return books;
    }

    @Override
    public List<BookResponseDto> getBooksByAuthor(Long authorId) {
        logger.info("Fetching books for Author ID: {}", authorId);

        if (!authorRepository.existsById(authorId)) {
            logger.warn("Author not found with ID: {}", authorId);
            throw new ResourceNotFoundException("Author not found.");
        }

        List<BookResponseDto> books = bookRepository.findByAuthorsAuthorId(authorId)
                .stream()
                .map(bookMapper::toResponseDto)
                .toList();

        logger.info("Retrieved {} books for Author ID: {}", books.size(), authorId);

        return books;
    }

    @Override
    public BookResponseDto adjustStock(Long bookId, Integer stock) {
        logger.info("Updating stock for Book ID: {}. New Stock: {}", bookId, stock);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.warn("Book not found with ID: {}", bookId);
                    return new ResourceNotFoundException("Book not found.");
                });

        book.setStock(stock);

        Book updatedBook = bookRepository.save(book);

        logger.info("Stock updated successfully for Book ID: {}. Current Stock: {}",
                updatedBook.getBookId(),
                updatedBook.getStock());

        return bookMapper.toResponseDto(updatedBook);
    }

    @Override
    public Double getAverageRating(Long bookId) {
        logger.info("Calculating average rating for Book ID: {}", bookId);

        if (!bookRepository.existsById(bookId)) {
            logger.warn("Book not found with ID: {}", bookId);
            throw new ResourceNotFoundException("Book not found.");
        }

        Double averageRating = reviewRepository.getAverageRating(bookId);

        Double rating =  averageRating == null ? 0.0 : averageRating;

        logger.info("Average rating for Book ID: {} is {}", bookId, rating);

        return rating;
    }
}

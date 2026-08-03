package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookResponseDto;
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

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final ReviewRepository reviewRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponseDto create(BookRequestDto requestDto) {
        if (bookRepository.existsByIsbn(requestDto.getIsbn())) {
            throw new DuplicateResourceException("ISBN already exists.");
        }

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        Publisher publisher = publisherRepository.findById(requestDto.getPublisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found."));

        Set<Author> authors = new HashSet<>(
                authorRepository.findAllById(requestDto.getAuthorIds())
        );

        if (authors.size() != requestDto.getAuthorIds().size()) {
            throw new ResourceNotFoundException("One or more authors not found.");
        }

        Book book = bookMapper.toEntity(requestDto);

        book.setCategory(category);
        book.setPublisher(publisher);
        book.setAuthors(authors);

        return bookMapper.toResponseDto(bookRepository.save(book));
    }

    @Override
    public BookResponseDto getById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));

        return bookMapper.toResponseDto(book);
    }

    @Override
    public Page<BookResponseDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toResponseDto);
    }

    @Override
    public BookResponseDto update(Long bookId, BookRequestDto requestDto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));

        if (!book.getIsbn().equals(requestDto.getIsbn()) && bookRepository.existsByIsbn(requestDto.getIsbn())) {
            throw new DuplicateResourceException("ISBN already exists.");
        }

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        Publisher publisher = publisherRepository.findById(requestDto.getPublisherId())
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found."));

        Set<Author> authors = new HashSet<>(
                authorRepository.findAllById(requestDto.getAuthorIds())
        );

        if (authors.size() != requestDto.getAuthorIds().size()) {
            throw new ResourceNotFoundException("One or more authors not found.");
        }

        bookMapper.updateEntity(requestDto, book);

        book.setCategory(category);
        book.setPublisher(publisher);
        book.setAuthors(authors);

        return bookMapper.toResponseDto(bookRepository.save(book));
    }

    @Override
    public void delete(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));

        book.setStatus(Status.DELETED);

        bookRepository.save(book);
    }

    @Override
    public Page<BookResponseDto> search(String title, Long categoryId, Long authorId, Double price, Boolean inStock, Pageable pageable) {
        Specification<Book> specification = Specification
                .where(BookSpecification.hasTitle(title))
                .and(BookSpecification.hasCategory(categoryId))
                .and(BookSpecification.hasAuthor(authorId))
                .and(BookSpecification.hasPrice(price))
                .and(BookSpecification.isInStock(inStock));

        return bookRepository.findAll(specification, pageable)
                .map(bookMapper::toResponseDto);
    }

    @Override
    public List<BookResponseDto> getBooksByAuthor(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Author not found.");
        }

        return bookRepository.findByAuthorsAuthorId(authorId)
                .stream()
                .map(bookMapper::toResponseDto)
                .toList();
    }

    @Override
    public BookResponseDto adjustStock(Long bookId, Integer stock) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));

        book.setStock(stock);

        return bookMapper.toResponseDto(bookRepository.save(book));
    }

    @Override
    public Double getAverageRating(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found.");
        }

        Double averageRating = reviewRepository.getAverageRating(bookId);

        return averageRating == null ? 0.0 : averageRating;
    }
}

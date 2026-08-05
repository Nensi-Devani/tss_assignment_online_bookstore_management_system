package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.author.AuthorRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.author.AuthorResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Author;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.DuplicateResourceException;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.AuthorMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.AuthorRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.AuthorService;
import com.bookstore.tss_assignment_online_bookstore_management_system.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    public AuthorResponseDto create(AuthorRequestDto requestDto) {
        logger.info("Creating author with name: {}", requestDto.getName());

        if (authorRepository.existsByName(requestDto.getName())) {
            logger.warn("Author already exists with name: {}", requestDto.getName());
            throw new DuplicateResourceException("Author already exists.");
        }

        Author author = authorMapper.toEntity(requestDto);
        Author savedAuthor = authorRepository.save(author);

        logger.info("Author created successfully with Name: {}, ID: {}",savedAuthor.getName(), savedAuthor.getAuthorId());

        return authorMapper.toResponseDto(savedAuthor);
    }

    @Override
    public AuthorResponseDto getById(Long authorId) {
        logger.info("Fetching author with ID: {}", authorId);

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                        logger.warn("Author not found with ID: {}", authorId);
                        return new ResourceNotFoundException("Author not found.");
                });

        logger.info("Author fetched successfully with ID: {}", authorId);

        return authorMapper.toResponseDto(author);
    }

    @Override
    public PageResponseDto<AuthorResponseDto> getAll(Pageable pageable) {
        logger.info("Fetching all authors. Page: {}, Size: {}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        Page<AuthorResponseDto> authors = authorRepository.findAll(pageable)
                .map(authorMapper::toResponseDto);

        logger.info("Fetched {} authors.", authors.getNumberOfElements());

        return PageUtil.toPageResponse(authors);
    }

    @Override
    public AuthorResponseDto update(Long authorId, AuthorRequestDto requestDto) {
        logger.info("Updating author with ID: {}", authorId);

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    logger.warn("Author not found with ID: {}", authorId);
                    return new ResourceNotFoundException("Author not found.");
                });

        if (!author.getName().equalsIgnoreCase(requestDto.getName()) && authorRepository.existsByName(requestDto.getName())) {
            logger.warn("Another author already exists with name: {}", requestDto.getName());
            throw new DuplicateResourceException("Author already exists.");
        }

        authorMapper.updateEntity(requestDto, author);
        Author updatedAuthor = authorRepository.save(author);

        logger.info("Author updated successfully with ID: {}", updatedAuthor.getAuthorId());

        return authorMapper.toResponseDto(updatedAuthor);
    }

    @Override
    public void delete(Long authorId) {
        logger.info("Deleting author with ID: {}", authorId);

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    logger.warn("Author not found with ID: {}", authorId);
                    return new ResourceNotFoundException("Author not found.");
                });

        author.setStatus(Status.DELETED);

        authorRepository.save(author);

        logger.info("Author marked as DELETED with ID: {}", authorId);
    }

    @Override
    public List<AuthorResponseDto> getAllActiveAuthors() {
        logger.info("Fetching all active authors.");

        List<AuthorResponseDto> authors = authorRepository.findByStatus(Status.ACTIVE)
                .stream()
                .map(authorMapper::toResponseDto)
                .toList();

        logger.info("Fetched {} active authors.", authors.size());

        return authors;
    }
}

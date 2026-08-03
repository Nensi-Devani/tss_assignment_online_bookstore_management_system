package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.author.AuthorRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.author.AuthorResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Author;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.DuplicateResourceException;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.AuthorMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.AuthorRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    public AuthorResponseDto create(AuthorRequestDto requestDto) {
        if (authorRepository.existsByName(requestDto.getName())) {
            throw new DuplicateResourceException("Author already exists.");
        }

        Author author = authorMapper.toEntity(requestDto);

        return authorMapper.toResponseDto(authorRepository.save(author));
    }

    @Override
    public AuthorResponseDto getById(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found."));

        return authorMapper.toResponseDto(author);
    }

    @Override
    public Page<AuthorResponseDto> getAll(Pageable pageable) {
        return authorRepository.findAll(pageable)
                .map(authorMapper::toResponseDto);
    }

    @Override
    public AuthorResponseDto update(Long authorId, AuthorRequestDto requestDto) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found."));

        if (!author.getName().equalsIgnoreCase(requestDto.getName()) && authorRepository.existsByName(requestDto.getName())) {
            throw new DuplicateResourceException("Author already exists.");
        }

        authorMapper.updateEntity(requestDto, author);

        return authorMapper.toResponseDto(authorRepository.save(author));
    }

    @Override
    public void delete(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found."));

        author.setStatus(Status.DELETED);

        authorRepository.save(author);
    }

    @Override
    public List<AuthorResponseDto> getAllActiveAuthors() {
        return authorRepository.findByStatus(Status.ACTIVE)
                .stream()
                .map(authorMapper::toResponseDto)
                .toList();
    }
}

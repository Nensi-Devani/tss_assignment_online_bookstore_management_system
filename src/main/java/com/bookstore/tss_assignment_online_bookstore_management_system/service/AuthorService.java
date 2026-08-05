package com.bookstore.tss_assignment_online_bookstore_management_system.service;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.author.AuthorRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.author.AuthorResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService {

    AuthorResponseDto create(AuthorRequestDto requestDto);

    AuthorResponseDto getById(Long authorId);

    PageResponseDto<AuthorResponseDto> getAll(Pageable pageable);

    AuthorResponseDto update(Long authorId, AuthorRequestDto requestDto);

    void delete(Long authorId);

    List<AuthorResponseDto> getAllActiveAuthors();

}

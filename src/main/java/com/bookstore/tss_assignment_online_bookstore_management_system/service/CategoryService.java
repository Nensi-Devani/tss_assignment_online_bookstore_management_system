package com.bookstore.tss_assignment_online_bookstore_management_system.service;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.category.CategoryRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.category.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryResponseDto create(CategoryRequestDto requestDto);

    CategoryResponseDto getById(Long categoryId);

    Page<CategoryResponseDto> getAll(Pageable pageable);

    CategoryResponseDto update(Long categoryId, CategoryRequestDto requestDto);

    void delete(Long categoryId);

}

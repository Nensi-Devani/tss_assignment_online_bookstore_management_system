package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.category.CategoryRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.category.CategoryResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Category;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.DuplicateResourceException;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.CategoryMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.CategoryRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto create(CategoryRequestDto requestDto) {
        if (categoryRepository.existsByName(requestDto.getName())) {
            throw new DuplicateResourceException("Category already exists.");
        }

        Category category = categoryMapper.toEntity(requestDto);

        return categoryMapper.toResponseDto(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDto getById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        return categoryMapper.toResponseDto(category);
    }

    @Override
    public Page<CategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponseDto);
    }

    @Override
    public CategoryResponseDto update(Long categoryId, CategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        if (!category.getName().equalsIgnoreCase(requestDto.getName()) && categoryRepository.existsByName(requestDto.getName())) {
            throw new DuplicateResourceException("Category already exists.");
        }

        categoryMapper.updateEntity(requestDto, category);

        return categoryMapper.toResponseDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        category.setStatus(Status.DELETED);

        categoryRepository.save(category);
    }
}

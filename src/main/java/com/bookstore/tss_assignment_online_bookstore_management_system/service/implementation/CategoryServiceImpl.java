package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.category.CategoryRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.category.CategoryResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Category;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.DuplicateResourceException;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.CategoryMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.CategoryRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.CategoryService;
import com.bookstore.tss_assignment_online_bookstore_management_system.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto create(CategoryRequestDto requestDto) {
        logger.info("Creating category. Name={}", requestDto.getName());

        if (categoryRepository.existsByName(requestDto.getName())) {
            logger.warn("Category already exists. Name={}", requestDto.getName());
            throw new DuplicateResourceException("Category already exists.");
        }

        Category category = categoryMapper.toEntity(requestDto);

        Category savedCategory = categoryRepository.save(category);

        logger.info("Category created successfully. CategoryId={}", savedCategory.getCategoryId());

        return categoryMapper.toResponseDto(savedCategory);
    }

    @Override
    public CategoryResponseDto getById(Long categoryId) {
        logger.info("Fetching category. CategoryId={}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    logger.warn("Category not found. CategoryId={}", categoryId);
                    return new ResourceNotFoundException("Category not found.");
                });

        logger.info("Category fetched successfully. CategoryId={}", categoryId);

        return categoryMapper.toResponseDto(category);
    }

    @Override
    public PageResponseDto<CategoryResponseDto> getAll(Pageable pageable) {
        logger.info("Fetching all categories. Page={}, Size={}",
                pageable.getPageNumber(),
                pageable.getPageSize());

        Page<CategoryResponseDto> categories =  categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponseDto);

        logger.info("Retrieved {} categories.", categories.getNumberOfElements());

        return PageUtil.toPageResponse(categories);
    }

    @Override
    public CategoryResponseDto update(Long categoryId, CategoryRequestDto requestDto) {
        logger.info("Updating category. CategoryId={}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->{
                    logger.warn("Category not found. CategoryId={}", categoryId);
                    return new ResourceNotFoundException("Category not found.");
                });

        if (!category.getName().equalsIgnoreCase(requestDto.getName()) && categoryRepository.existsByName(requestDto.getName())) {
            logger.warn("Category already exists. Name={}", requestDto.getName());
            throw new DuplicateResourceException("Category already exists.");
        }

        categoryMapper.updateEntity(requestDto, category);

        Category updatedCategory = categoryRepository.save(category);

        logger.info("Category updated successfully. CategoryId={}", updatedCategory.getCategoryId());

        return categoryMapper.toResponseDto(updatedCategory);
    }

    @Override
    public void delete(Long categoryId) {
        logger.info("Deleting category. CategoryId={}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    logger.warn("Category not found. CategoryId={}", categoryId);
                    return new ResourceNotFoundException("Category not found.");
                });

        category.setStatus(Status.DELETED);

        categoryRepository.save(category);

        logger.info("Category marked as DELETED. CategoryId={}", categoryId);
    }
}

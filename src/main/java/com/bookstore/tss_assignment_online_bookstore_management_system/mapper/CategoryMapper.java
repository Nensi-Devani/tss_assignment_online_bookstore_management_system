package com.bookstore.tss_assignment_online_bookstore_management_system.mapper;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.category.CategoryRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.category.CategoryResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequestDto dto);

    CategoryResponseDto toResponseDto(Category category);

    void updateEntity(CategoryRequestDto dto, @MappingTarget Category category);

}

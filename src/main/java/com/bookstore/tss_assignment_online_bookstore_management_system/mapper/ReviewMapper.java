package com.bookstore.tss_assignment_online_bookstore_management_system.mapper;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.review.ReviewRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.review.ReviewResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", ignore = true)
    Review toEntity(ReviewRequestDto dto);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "book.bookId", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    ReviewResponseDto toResponseDto(Review review);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", ignore = true)
    void updateEntity(ReviewRequestDto dto, @MappingTarget Review review);

}

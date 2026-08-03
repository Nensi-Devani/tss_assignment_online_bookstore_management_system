package com.bookstore.tss_assignment_online_bookstore_management_system.mapper;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.book.BookResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                CategoryMapper.class,
                PublisherMapper.class,
                AuthorMapper.class
        }
)
public interface BookMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "authors", ignore = true)
    Book toEntity(BookRequestDto dto);

    @Mapping(target = "averageRating", ignore = true)
    BookResponseDto toResponseDto(Book book);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "authors", ignore = true)
    void updateEntity(BookRequestDto dto, @MappingTarget Book book);

}

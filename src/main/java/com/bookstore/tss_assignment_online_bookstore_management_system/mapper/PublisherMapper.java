package com.bookstore.tss_assignment_online_bookstore_management_system.mapper;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher.PublisherRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher.PublisherResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Publisher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PublisherMapper {

    Publisher toEntity(PublisherRequestDto dto);

    PublisherResponseDto toResponseDto(Publisher publisher);

    void updateEntity(PublisherRequestDto dto, @MappingTarget Publisher publisher);

}

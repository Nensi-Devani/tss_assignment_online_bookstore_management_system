package com.bookstore.tss_assignment_online_bookstore_management_system.mapper;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.user.UserRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.user.UserResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestDto dto);

    UserResponseDto toResponseDto(User user);

    void updateEntity(UserRequestDto dto, @MappingTarget User user);

}

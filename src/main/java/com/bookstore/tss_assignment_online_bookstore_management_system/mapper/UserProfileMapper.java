package com.bookstore.tss_assignment_online_bookstore_management_system.mapper;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.userprofile.UserProfileRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.userprofile.UserProfileResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "user", ignore = true)
    UserProfile toEntity(UserProfileRequestDto dto);

    @Mapping(source = "user.userId", target = "userId")
    UserProfileResponseDto toResponseDto(UserProfile userProfile);

    @Mapping(target = "user", ignore = true)
    void updateEntity(UserProfileRequestDto dto, @MappingTarget UserProfile userProfile);

}

package com.bookstore.tss_assignment_online_bookstore_management_system.service;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.userprofile.UserProfileRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.userprofile.UserProfileResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserProfileService {

    UserProfileResponseDto create(Long userId, UserProfileRequestDto requestDto);

    UserProfileResponseDto getByUserId(Long userId);

    PageResponseDto<UserProfileResponseDto> getAll(Pageable pageable);

    UserProfileResponseDto update(Long userId, UserProfileRequestDto requestDto);

    void deleteByUserId(Long userId);
}

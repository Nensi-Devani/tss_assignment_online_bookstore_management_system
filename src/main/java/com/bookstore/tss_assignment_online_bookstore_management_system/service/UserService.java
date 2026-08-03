package com.bookstore.tss_assignment_online_bookstore_management_system.service;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.user.UserRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.user.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDto create(UserRequestDto requestDto);

    UserResponseDto getById(Long userId);

    Page<UserResponseDto> getAll(Pageable pageable);

    UserResponseDto update(Long userId, UserRequestDto requestDto);

    void delete(Long userId);
}

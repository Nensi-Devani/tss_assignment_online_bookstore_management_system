package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.user.UserRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.user.UserResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.User;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.DuplicateResourceException;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.UserMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.UserRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto create(UserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        User user = userMapper.toEntity(requestDto);

        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto getById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        return userMapper.toResponseDto(user);
    }

    @Override
    public Page<UserResponseDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponseDto);
    }

    @Override
    public UserResponseDto update(Long userId, UserRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (!user.getEmail().equalsIgnoreCase(requestDto.getEmail()) && userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        userMapper.updateEntity(requestDto, user);

        return userMapper.toResponseDto(userRepository.save(user));
    }

    @Override
    public void delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        user.setStatus(Status.DELETED);

        userRepository.save(user);
    }
}

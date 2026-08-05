package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.user.UserRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.user.UserResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.user.UserUpdateRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.User;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.DuplicateResourceException;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.UserMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.UserRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.UserService;
import com.bookstore.tss_assignment_online_bookstore_management_system.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto create(UserRequestDto requestDto) {
        logger.info("Creating user. Email={}", requestDto.getEmail());

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            logger.warn("Email already exists. Email={}", requestDto.getEmail());
            throw new DuplicateResourceException("Email already exists.");
        }

        User user = userMapper.toEntity(requestDto);

        User savedUser = userRepository.save(user);

        logger.info("User created successfully. UserId={}", savedUser.getUserId());

        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public UserResponseDto getById(Long userId) {
        logger.info("Fetching user. UserId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found. UserId={}", userId);
                    return new ResourceNotFoundException("User not found.");
                });

        logger.info("User fetched successfully. UserId={}", userId);

        return userMapper.toResponseDto(user);
    }

    @Override
    public PageResponseDto<UserResponseDto> getAll(Pageable pageable) {
        logger.info(
                "Fetching all users. Page={}, Size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<UserResponseDto> users = userRepository.findAll(pageable)
                .map(userMapper::toResponseDto);

        logger.info("Retrieved {} users.", users.getNumberOfElements());

        return PageUtil.toPageResponse(users);
    }

    @Override
    public UserResponseDto update(Long userId, UserUpdateRequestDto requestDto) {
        logger.info("Updating user. UserId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found. UserId={}", userId);
                    return new ResourceNotFoundException("User not found.");
                });

        userMapper.updateEntity(requestDto, user);

        User updatedUser = userRepository.save(user);

        logger.info("User updated successfully. UserId={}", updatedUser.getUserId());

        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    public void delete(Long userId) {
        logger.info("Deleting user. UserId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found. UserId={}", userId);
                    return new ResourceNotFoundException("User not found.");
                });

        user.setStatus(Status.DELETED);

        userRepository.save(user);

        logger.info("User marked as DELETED. UserId={}", userId);
    }
}

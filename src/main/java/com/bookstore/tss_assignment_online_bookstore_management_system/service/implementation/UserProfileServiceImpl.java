package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.userprofile.UserProfileRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.userprofile.UserProfileResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.User;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.UserProfile;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.DuplicateResourceException;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.UserProfileMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.UserProfileRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.UserRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfileResponseDto create(Long userId, UserProfileRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        if (userProfileRepository.existsByUser(user)) {
            throw new DuplicateResourceException("User profile already exists.");
        }

        UserProfile userProfile = userProfileMapper.toEntity(requestDto);
        userProfile.setUser(user);

        return userProfileMapper.toResponseDto(userProfileRepository.save(userProfile));
    }

    @Override
    public UserProfileResponseDto getByUserId(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found."));

        return userProfileMapper.toResponseDto(userProfile);
    }

    @Override
    public Page<UserProfileResponseDto> getAll(Pageable pageable) {
        return userProfileRepository.findAll(pageable)
                .map(userProfileMapper::toResponseDto);
    }

    @Override
    public UserProfileResponseDto update(Long userId, UserProfileRequestDto requestDto) {
        UserProfile userProfile = userProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found."));

        userProfileMapper.updateEntity(requestDto, userProfile);
        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toResponseDto(userProfile);
    }

    @Override
    public void deleteByUserId(Long userId) {
        UserProfile userProfile = userProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found."));

        userProfileRepository.delete(userProfile);
    }
}

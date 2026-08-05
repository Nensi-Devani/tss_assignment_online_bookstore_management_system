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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public UserProfileResponseDto create(Long userId, UserProfileRequestDto requestDto) {
        logger.info("Creating user profile. UserId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found. UserId={}", userId);
                    return new ResourceNotFoundException("User not found.");
                });

        if (userProfileRepository.existsByUser(user)) {
            logger.warn("User profile already exists. UserId={}", userId);
            throw new DuplicateResourceException("User profile already exists.");
        }

        UserProfile userProfile = userProfileMapper.toEntity(requestDto);
        userProfile.setUser(user);

        UserProfile savedProfile = userProfileRepository.save(userProfile);

        logger.info("User profile created successfully. UserProfileId={}, UserId={}",
                savedProfile.getUserProfileId(),
                userId);

        return userProfileMapper.toResponseDto(savedProfile);
    }

    @Override
    public UserProfileResponseDto getByUserId(Long userId) {
        logger.info("Fetching user profile. UserId={}", userId);

        UserProfile userProfile = userProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> {
                    logger.warn("User profile not found. UserId={}", userId);
                    return new ResourceNotFoundException("User profile not found.");
                });

        logger.info("User profile fetched successfully. UserId={}", userId);

        return userProfileMapper.toResponseDto(userProfile);
    }

    @Override
    public Page<UserProfileResponseDto> getAll(Pageable pageable) {
        logger.info(
                "Fetching all user profiles. Page={}, Size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<UserProfileResponseDto> profiles = userProfileRepository.findAll(pageable)
                .map(userProfileMapper::toResponseDto);

        logger.info("Retrieved {} user profiles.", profiles.getNumberOfElements());

        return profiles;
    }

    @Override
    public UserProfileResponseDto update(Long userId, UserProfileRequestDto requestDto) {
        logger.info("Updating user profile. UserId={}", userId);

        UserProfile userProfile = userProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> {
                    logger.warn("User profile not found. UserId={}", userId);
                    return new ResourceNotFoundException("User profile not found.");
                });

        userProfileMapper.updateEntity(requestDto, userProfile);
        userProfile = userProfileRepository.save(userProfile);

        logger.info("User profile updated successfully. UserProfileId={}, UserId={}",
                userProfile.getUserProfileId(),
                userId);

        return userProfileMapper.toResponseDto(userProfile);
    }

    @Override
    public void deleteByUserId(Long userId) {
        logger.info("Deleting user profile. UserId={}", userId);

        UserProfile userProfile = userProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> {
                    logger.warn("User profile not found. UserId={}", userId);
                    return new ResourceNotFoundException("User profile not found.");
                });

        userProfileRepository.delete(userProfile);

        logger.info("User profile deleted successfully. UserId={}", userId);
    }
}

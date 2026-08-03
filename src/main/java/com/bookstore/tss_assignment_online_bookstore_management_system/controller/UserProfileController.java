package com.bookstore.tss_assignment_online_bookstore_management_system.controller;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.userprofile.UserProfileRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.userprofile.UserProfileResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<UserProfileResponseDto> create(@PathVariable Long userId, @Valid @RequestBody UserProfileRequestDto requestDto) {
        return new ResponseEntity<>(userProfileService.create(userId, requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<UserProfileResponseDto> getById(@PathVariable Long profileId) {
        return ResponseEntity.ok(userProfileService.getById(profileId));
    }

    @GetMapping
    public ResponseEntity<Page<UserProfileResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(userProfileService.getAll(pageable));
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<UserProfileResponseDto> update(@PathVariable Long userId, @Valid @RequestBody UserProfileRequestDto requestDto) {
        return ResponseEntity.ok(userProfileService.update(userId, requestDto));
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> delete(@PathVariable Long profileId) {
        userProfileService.delete(profileId);
        return ResponseEntity.noContent().build();
    }
}

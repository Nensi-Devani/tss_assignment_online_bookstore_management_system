package com.bookstore.tss_assignment_online_bookstore_management_system.dto.userprofile;

import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileResponseDto {

    private Long userProfileId;

    private Long userId;

    private String phone;

    private String address;

    private LocalDate dateOfBirth;

    private String avatarPath;

    private Status status;
}

package com.bookstore.tss_assignment_online_bookstore_management_system.dto.userprofile;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileRequestDto {

    @NotBlank(message = "Phone number is required.")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit Indian mobile number."
    )
    private String phone;

    @NotBlank(message = "Address is required.")
    @Size(max = 500, message = "Address cannot exceed 500 characters.")
    private String address;

    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth must be in the past.")
    private LocalDate dateOfBirth;

    @Size(max = 255, message = "Avatar path cannot exceed 255 characters.")
    private String avatarPath;

    @NotNull(message = "User ID is required.")
    private Long userId;

}

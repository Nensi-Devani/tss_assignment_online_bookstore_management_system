package com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PublisherRequestDto {

    @NotBlank(message = "Publisher name is required.")
    @Size(min = 2, max = 50, message = "Publisher name must be between 2 and 50 characters.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    @Size(max = 50, message = "Email cannot exceed 50 characters.")
    private String email;

    @NotBlank(message = "Phone number is required.")
    @Pattern(
            regexp = "^\\d{10}$",
            message = "Phone number must be a valid 10-digit Indian mobile number."
    )
    private String phone;

    @NotBlank(message = "Address is required.")
    @Size(max = 500, message = "Address cannot exceed 500 characters.")
    private String address;

}

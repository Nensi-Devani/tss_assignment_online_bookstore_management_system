package com.bookstore.tss_assignment_online_bookstore_management_system.dto.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthorRequestDto {

    @NotBlank(message = "Author name is required.")
    @Size(min = 2, max = 50, message = "Author name must be between 2 and 50 characters.")
    private String name;

    @Size(max = 500, message = "Biography cannot exceed 500 characters.")
    private String biography;

}

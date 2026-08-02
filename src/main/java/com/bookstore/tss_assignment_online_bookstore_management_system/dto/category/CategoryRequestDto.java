package com.bookstore.tss_assignment_online_bookstore_management_system.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {

    @NotBlank(message = "Category name is required.")
    @Size(max = 50, message = "Category name cannot exceed 50 characters.")
    private String name;

}

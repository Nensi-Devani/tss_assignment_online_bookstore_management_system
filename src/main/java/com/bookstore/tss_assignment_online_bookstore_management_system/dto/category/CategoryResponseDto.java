package com.bookstore.tss_assignment_online_bookstore_management_system.dto.category;

import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import lombok.Data;

@Data
public class CategoryResponseDto {

    private Long categoryId;

    private String name;

    private Status status;

}

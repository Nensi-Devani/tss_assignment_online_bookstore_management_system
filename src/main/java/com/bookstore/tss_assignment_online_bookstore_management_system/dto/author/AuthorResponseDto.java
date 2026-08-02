package com.bookstore.tss_assignment_online_bookstore_management_system.dto.author;

import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import lombok.Data;

@Data
public class AuthorResponseDto {

    private Long authorId;

    private String name;

    private String biography;

    private Status status;

}

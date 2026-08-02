package com.bookstore.tss_assignment_online_bookstore_management_system.dto.user;

import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import lombok.Data;

@Data
public class UserResponseDto {

    private Long userId;

    private String name;

    private String email;

    private Status status;

}

package com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher;

import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import lombok.Data;

@Data
public class PublisherResponseDto {

    private Long publisherId;

    private String name;

    private String email;

    private String phone;

    private String address;

    private Status status;

}

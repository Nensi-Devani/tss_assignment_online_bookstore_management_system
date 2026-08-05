package com.bookstore.tss_assignment_online_bookstore_management_system.dto.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponseDto<T> {

    private List<T> content;

    private int numberOfElements;

    private int pageNumber;

    private int pageSize;

    private long totalElements;

    private int totalPages;
}

package com.bookstore.tss_assignment_online_bookstore_management_system.dto.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {

    private List<T> content;

    private long totalElements;

    private int totalPages;

    private int currentPage;

    private int pageSize;

    private boolean first;

    private boolean last;

}

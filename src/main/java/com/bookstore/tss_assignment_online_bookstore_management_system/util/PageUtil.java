package com.bookstore.tss_assignment_online_bookstore_management_system.util;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
import org.springframework.data.domain.Page;

public class PageUtil {

    private PageUtil() {
    }

    public static <T> PageResponseDto<T> toPageResponse(Page<T> page) {

        return PageResponseDto.<T>builder()
                .content(page.getContent())
                .numberOfElements(page.getNumberOfElements())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}

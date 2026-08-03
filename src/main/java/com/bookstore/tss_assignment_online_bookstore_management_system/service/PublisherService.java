package com.bookstore.tss_assignment_online_bookstore_management_system.service;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher.PublisherRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher.PublisherResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublisherService {

    PublisherResponseDto create(PublisherRequestDto requestDto);

    PublisherResponseDto getById(Long publisherId);

    Page<PublisherResponseDto> getAll(Pageable pageable);

    PublisherResponseDto update(Long publisherId, PublisherRequestDto requestDto);

    void delete(Long publisherId);

}

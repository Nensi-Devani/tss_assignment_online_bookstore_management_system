package com.bookstore.tss_assignment_online_bookstore_management_system.controller;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher.PublisherRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.publisher.PublisherResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @PostMapping
    public ResponseEntity<PublisherResponseDto> create(@Valid @RequestBody PublisherRequestDto requestDto) {
        return new ResponseEntity<>(publisherService.create(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{publisherId}")
    public ResponseEntity<PublisherResponseDto> getById(@PathVariable Long publisherId) {
        return ResponseEntity.ok(publisherService.getById(publisherId));
    }

    @GetMapping
    public ResponseEntity<Page<PublisherResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(publisherService.getAll(pageable));
    }

    @PutMapping("/{publisherId}")
    public ResponseEntity<PublisherResponseDto> update(@PathVariable Long publisherId, @Valid @RequestBody PublisherRequestDto requestDto) {
        return ResponseEntity.ok(publisherService.update(publisherId, requestDto));
    }

    @DeleteMapping("/{publisherId}")
    public ResponseEntity<Void> delete(@PathVariable Long publisherId) {
        publisherService.delete(publisherId);
        return ResponseEntity.noContent().build();
    }
}

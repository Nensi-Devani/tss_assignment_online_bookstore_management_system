package com.bookstore.tss_assignment_online_bookstore_management_system.controller;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.author.AuthorRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.author.AuthorResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorResponseDto> create(@Valid @RequestBody AuthorRequestDto requestDto){
        return new ResponseEntity<>(authorService.create(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDto> getById(@PathVariable Long authorId){
        return new ResponseEntity<>(authorService.getById(authorId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<AuthorResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(authorService.getAll(pageable));
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<AuthorResponseDto> update(@PathVariable Long authorId, @Valid @RequestBody AuthorRequestDto requestDto) {
        return ResponseEntity.ok(authorService.update(authorId, requestDto));
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> delete(@PathVariable Long authorId) {
        authorService.delete(authorId);
        return ResponseEntity.noContent().build();
    }
}

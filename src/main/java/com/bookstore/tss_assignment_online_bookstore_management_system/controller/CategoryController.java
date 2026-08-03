package com.bookstore.tss_assignment_online_bookstore_management_system.controller;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.category.CategoryRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.category.CategoryResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryRequestDto requestDto){
        return new ResponseEntity<>(categoryService.create(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable Long categoryId){
        return new ResponseEntity<>(categoryService.getById(categoryId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> getAll(Pageable pageable){
        return new ResponseEntity<>(categoryService.getAll(pageable), HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable Long categoryId, @Valid @RequestBody CategoryRequestDto requestDto){
        return new ResponseEntity<>(categoryService.update(categoryId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(@PathVariable Long categoryId){
        categoryService.delete(categoryId);
        return ResponseEntity.noContent().build();
    }
}

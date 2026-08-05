package com.bookstore.tss_assignment_online_bookstore_management_system.controller;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.payment.PaymentRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.payment.PaymentResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> create(@Valid @RequestBody PaymentRequestDto requestDto) {
        return new ResponseEntity<>(paymentService.create(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> getById(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getById(paymentId));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<PaymentResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(paymentService.getAll(pageable));
    }
}

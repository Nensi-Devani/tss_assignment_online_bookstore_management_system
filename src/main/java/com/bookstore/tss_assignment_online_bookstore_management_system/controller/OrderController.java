package com.bookstore.tss_assignment_online_bookstore_management_system.controller;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.order.OrderRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.order.OrderResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(@Valid @RequestBody OrderRequestDto requestDto) {
        return new ResponseEntity<>(orderService.placeOrder(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getById(orderId));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(orderService.getAll(pageable));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrderResponseDto>> getOrdersByUser(@PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userId, pageable));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
}

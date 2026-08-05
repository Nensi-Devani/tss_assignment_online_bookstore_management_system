package com.bookstore.tss_assignment_online_bookstore_management_system.service;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.payment.PaymentRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.payment.PaymentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    PaymentResponseDto create(PaymentRequestDto requestDto);

    PaymentResponseDto getById(Long paymentId);

    PageResponseDto<PaymentResponseDto> getAll(Pageable pageable);
}

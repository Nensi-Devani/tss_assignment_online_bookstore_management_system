package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.payment.PaymentRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.payment.PaymentResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Order;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Payment;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.OrderStatus;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.PaymentStatus;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.PaymentMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.OrderRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.PaymentRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponseDto create(PaymentRequestDto requestDto) {
        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        Payment payment = paymentMapper.toEntity(requestDto);

        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId(UUID.randomUUID().toString());

        order.setOrderStatus(OrderStatus.COMPLETED);

        orderRepository.save(order);

        return paymentMapper.toResponseDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponseDto getById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found."));

        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public Page<PaymentResponseDto> getAll(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(paymentMapper::toResponseDto);
    }
}

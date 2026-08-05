package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
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
import com.bookstore.tss_assignment_online_bookstore_management_system.util.PageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponseDto create(PaymentRequestDto requestDto) {
        logger.info("Processing payment for OrderId={}", requestDto.getOrderId());

        Order order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> {
                    logger.warn("Order not found. OrderId={}", requestDto.getOrderId());
                    return new ResourceNotFoundException("Order not found.");
                });

        Payment payment = paymentMapper.toEntity(requestDto);

        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setTransactionId(UUID.randomUUID().toString());

        order.setOrderStatus(OrderStatus.COMPLETED);

        orderRepository.save(order);

        Payment savedPayment = paymentRepository.save(payment);

        logger.info(
                "Payment completed successfully. PaymentId={}, OrderId={}, Amount={}, TransactionId={}",
                savedPayment.getPaymentId(),
                order.getOrderId(),
                savedPayment.getAmount(),
                savedPayment.getTransactionId()
        );

        return paymentMapper.toResponseDto(savedPayment);
    }

    @Override
    public PaymentResponseDto getById(Long paymentId) {
        logger.info("Fetching payment. PaymentId={}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> {
                    logger.warn("Payment not found. PaymentId={}", paymentId);
                    return new ResourceNotFoundException("Payment not found.");
                });

        logger.info("Payment fetched successfully. PaymentId={}", paymentId);

        return paymentMapper.toResponseDto(payment);
    }

    @Override
    public PageResponseDto<PaymentResponseDto> getAll(Pageable pageable) {
        logger.info(
                "Fetching all payments. Page={}, Size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<PaymentResponseDto> payments = paymentRepository.findAll(pageable)
                .map(paymentMapper::toResponseDto);

        logger.info("Retrieved {} payments.", payments.getNumberOfElements());

        return PageUtil.toPageResponse(payments);
    }
}

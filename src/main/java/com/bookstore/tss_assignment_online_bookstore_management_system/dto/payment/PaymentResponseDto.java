package com.bookstore.tss_assignment_online_bookstore_management_system.dto.payment;

import com.bookstore.tss_assignment_online_bookstore_management_system.enums.PaymentMethod;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {

    private Long paymentId;

    private Long orderId;

    private Double amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private String transactionId;

    private LocalDateTime paymentDate;

}

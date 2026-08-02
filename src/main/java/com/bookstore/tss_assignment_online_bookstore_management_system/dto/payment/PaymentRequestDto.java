package com.bookstore.tss_assignment_online_bookstore_management_system.dto.payment;

import com.bookstore.tss_assignment_online_bookstore_management_system.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDto {

    @NotNull(message = "Order ID is required.")
    private Long orderId;

    @NotNull(message = "Payment method is required.")
    private PaymentMethod paymentMethod;

}

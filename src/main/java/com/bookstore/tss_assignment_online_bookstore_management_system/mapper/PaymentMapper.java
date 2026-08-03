package com.bookstore.tss_assignment_online_bookstore_management_system.mapper;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.payment.PaymentRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.payment.PaymentResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    Payment toEntity(PaymentRequestDto dto);

    @Mapping(source = "order.orderId", target = "orderId")
    PaymentResponseDto toResponseDto(Payment payment);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "amount", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    void updateEntity(PaymentRequestDto dto, @MappingTarget Payment payment);

}

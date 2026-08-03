package com.bookstore.tss_assignment_online_bookstore_management_system.mapper;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.order.OrderItemRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.order.OrderItemResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "book", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "priceAtPurchase", ignore = true)
    OrderItem toEntity(OrderItemRequestDto dto);

    @Mapping(source = "book.bookId", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    OrderItemResponseDto toResponseDto(OrderItem orderItem);

    @Mapping(target = "book", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "priceAtPurchase", ignore = true)
    void updateEntity(OrderItemRequestDto dto, @MappingTarget OrderItem orderItem);

}

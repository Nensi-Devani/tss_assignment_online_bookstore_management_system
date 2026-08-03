package com.bookstore.tss_assignment_online_bookstore_management_system.mapper;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.order.OrderRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.order.OrderResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                OrderItemMapper.class
        }
)
public interface OrderMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "payment", ignore = true)
    Order toEntity(OrderRequestDto dto);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    OrderResponseDto toResponseDto(Order order);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "payment", ignore = true)
    void updateEntity(OrderRequestDto dto, @MappingTarget Order order);

}

package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.order.OrderItemRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.order.OrderRequestDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.dto.order.OrderResponseDto;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Book;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.Order;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.OrderItem;
import com.bookstore.tss_assignment_online_bookstore_management_system.entity.User;
import com.bookstore.tss_assignment_online_bookstore_management_system.enums.OrderStatus;
import com.bookstore.tss_assignment_online_bookstore_management_system.exception.ResourceNotFoundException;
import com.bookstore.tss_assignment_online_bookstore_management_system.mapper.OrderMapper;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.BookRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.OrderItemRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.OrderRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.repository.UserRepository;
import com.bookstore.tss_assignment_online_bookstore_management_system.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(OrderRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        double totalAmount = 0.0;

        order = orderRepository.save(order);

        for (OrderItemRequestDto itemDto : requestDto.getOrderItems()) {
            Book book = bookRepository.findById(itemDto.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found."));

            if (book.getStock() < itemDto.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for book : " + book.getTitle());
            }

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPriceAtPurchase(book.getPrice());

            double subtotal = book.getPrice() * itemDto.getQuantity();
            orderItem.setSubtotal(subtotal);

            totalAmount += subtotal;

            orderItemRepository.save(orderItem);

            book.setStock(book.getStock() - itemDto.getQuantity());
            bookRepository.save(book);
        }

        order.setTotalAmount(totalAmount);

        return orderMapper.toResponseDto(orderRepository.save(order));
    }

    @Override
    public OrderResponseDto getById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        return orderMapper.toResponseDto(order);
    }

    @Override
    public Page<OrderResponseDto> getAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toResponseDto);
    }

    @Override
    public Page<OrderResponseDto> getOrdersByUser(Long userId, Pageable pageable) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found.");
        }

        return orderRepository.findByUserUserId(userId, pageable)
                .map(orderMapper::toResponseDto);
    }

    @Override
    @Transactional
    public OrderResponseDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Order is already cancelled.");
        }

        for (OrderItem orderItem : order.getOrderItems()) {
            Book book = orderItem.getBook();
            book.setStock(book.getStock() + orderItem.getQuantity());

            bookRepository.save(book);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        return orderMapper.toResponseDto(orderRepository.save(order));
    }
}

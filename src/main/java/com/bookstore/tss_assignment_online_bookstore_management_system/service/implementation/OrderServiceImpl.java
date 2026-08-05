package com.bookstore.tss_assignment_online_bookstore_management_system.service.implementation;

import com.bookstore.tss_assignment_online_bookstore_management_system.dto.common.PageResponseDto;
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
import com.bookstore.tss_assignment_online_bookstore_management_system.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(OrderRequestDto requestDto) {
        logger.info("Placing order for UserId={}", requestDto.getUserId());

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> {
                    logger.warn("User not found. UserId={}", requestDto.getUserId());
                    return new ResourceNotFoundException("User not found.");
                });

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        double totalAmount = 0.0;

        order = orderRepository.save(order);

        for (OrderItemRequestDto itemDto : requestDto.getOrderItems()) {
            Book book = bookRepository.findById(itemDto.getBookId())
                    .orElseThrow(() -> {
                        logger.warn("Book not found. BookId={}", itemDto.getBookId());
                        return new ResourceNotFoundException("Book not found.");
                    });

            if (book.getStock() < itemDto.getQuantity()) {
                logger.warn(
                        "Insufficient stock. BookId={}, Available={}, Requested={}",
                        book.getBookId(),
                        book.getStock(),
                        itemDto.getQuantity()
                );
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

        Order savedOrder = orderRepository.save(order);

        logger.info(
                "Order placed successfully. OrderId={}, UserId={}, TotalAmount={}",
                savedOrder.getOrderId(),
                user.getUserId(),
                savedOrder.getTotalAmount()
        );

        return orderMapper.toResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDto getById(Long orderId) {
        logger.info("Fetching order. OrderId={}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.warn("Order not found. OrderId={}", orderId);
                    return new ResourceNotFoundException("Order not found.");
                });

        logger.info("Order fetched successfully. OrderId={}", orderId);

        return orderMapper.toResponseDto(order);
    }

    @Override
    public PageResponseDto<OrderResponseDto> getAll(Pageable pageable) {
        logger.info(
                "Fetching all orders. Page={}, Size={}",
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<OrderResponseDto> orders = orderRepository.findAll(pageable)
                .map(orderMapper::toResponseDto);

        logger.info("Retrieved {} orders.", orders.getNumberOfElements());

        return PageUtil.toPageResponse(orders);
    }

    @Override
    public PageResponseDto<OrderResponseDto> getOrdersByUser(Long userId, Pageable pageable) {
        logger.info("Fetching orders for UserId={}", userId);

        if (!userRepository.existsById(userId)) {
            logger.warn("User not found. UserId={}", userId);
            throw new ResourceNotFoundException("User not found.");
        }

        Page<OrderResponseDto> orders =  orderRepository.findByUserUserId(userId, pageable)
                .map(orderMapper::toResponseDto);

        logger.info(
                "Retrieved {} orders for UserId={}",
                orders.getNumberOfElements(),
                userId
        );

        return PageUtil.toPageResponse(orders);
    }

    @Override
    @Transactional
    public OrderResponseDto cancelOrder(Long orderId) {
        logger.info("Cancelling order. OrderId={}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.warn("Order not found. OrderId={}", orderId);
                    return new ResourceNotFoundException("Order not found.");
                });

        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            logger.warn("Order is already cancelled. OrderId={}", orderId);
            throw new IllegalArgumentException("Order is already cancelled.");
        }

        for (OrderItem orderItem : order.getOrderItems()) {
            Book book = orderItem.getBook();
            book.setStock(book.getStock() + orderItem.getQuantity());

            bookRepository.save(book);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);

        Order cancelledOrder = orderRepository.save(order);

        logger.info("Order cancelled successfully. OrderId={}", cancelledOrder.getOrderId());

        return orderMapper.toResponseDto(cancelledOrder);
    }
}

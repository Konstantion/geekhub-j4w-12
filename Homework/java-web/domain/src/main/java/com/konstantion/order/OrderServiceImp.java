package com.konstantion.order;

import com.konstantion.bucket.Bucket;
import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.order.dto.OrderDto;
import com.konstantion.order.validator.OrderValidator;
import com.konstantion.user.User;
import com.konstantion.user.UserRepository;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

public record OrderServiceImp(
        OrderRepository orderRepository,
        OrderValidator orderValidator,
        UserRepository userRepository
) implements OrderService {
    private static final OrderMapper orderMapper = OrderMapper.INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImp.class);


    @Override
    public List<OrderDto> findAll() {
        return orderMapper.toDto(orderRepository.findAll());
    }

    @Override
    public OrderDto createOrder(User user, Bucket bucket) {
        Order order = Order.builder()
                .uuid(UUID.randomUUID())
                .userUuid(user.uuid())
                .products(bucket.products())
                .totalPrice(bucket.getTotalPrice())
                .placedAt(LocalDateTime.now())
                .status(OrderStatus.NEW)
                .build();

        ValidationResult validationResult = orderValidator.isOrderValid(order);

        if (validationResult.errorsPresent()) {
            logger.warn("Failed to create order, given data is invalid {}",
                    order, validationResult.getErrorsAsMap());
            throw new ValidationException("Failed to create order, given data is invalid",
                    validationResult.getErrorsAsMap());
        }

        order = orderRepository.save(order);
        bucket.clear();

        logger.info("Order {} successfully created", orderMapper.toDto(order));

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto findOrderById(UUID uuid) {
        Order order = orderRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("Order with uuid %s doesn't exist", uuid)
        ));
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto deleteOrderById(UUID uuid) {
        return null;
    }

    @Override
    public List<OrderDto> findOrdersByUserId(UUID uuid) {
        userRepository.findUserById(uuid).orElseThrow(() ->
                new BadRequestException(format("User with uuid %s doesn't exist", uuid)
        ));
        List<Order> orders = orderRepository.findByUserId(uuid);

        return orderMapper.toDto(orders);
    }

    @Override
    public void completeOrder(UUID uuid) {
        orderRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("Order with uuid %s doesn't exist", uuid)
        ));

        orderRepository.updateOrderStatusById(uuid, OrderStatus.COMPLETED);
        logger.info("Order with id {} successfully completed", uuid);
    }
}

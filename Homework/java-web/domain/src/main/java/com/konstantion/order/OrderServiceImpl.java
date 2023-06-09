package com.konstantion.order;

import com.konstantion.bucket.Bucket;
import com.konstantion.exceptions.BadRequestException;
import com.konstantion.exceptions.ForbiddenException;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.order.validator.OrderValidator;
import com.konstantion.product.Product;
import com.konstantion.product.ProductRepository;
import com.konstantion.user.User;
import com.konstantion.user.UserRepository;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.konstantion.user.Role.ADMIN;
import static java.lang.String.format;

public record OrderServiceImpl(
        OrderRepository orderRepository,
        OrderValidator orderValidator,
        UserRepository userRepository,
        ProductRepository productRepository
) implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order createOrder(User user, Bucket bucket) {
        Map<Product, Integer> products = extractBucketProducts(bucket);
        double totalPrice = products.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().price() * entry.getValue())
                .sum();
        Order order = Order.builder()
                .uuid(UUID.randomUUID())
                .userUuid(user.getId())
                .products(products)
                .totalPrice(totalPrice)
                .placedAt(LocalDateTime.now())
                .status(OrderStatus.NEW)
                .build();

        ValidationResult validationResult = orderValidator.isOrderValid(order);

        if (validationResult.errorsPresent()) {
            logger.warn("Failed to create order, given data is invalid {}",
                    order);
            throw new ValidationException("Failed to create order, given data is invalid",
                    validationResult.getErrorsAsMap());
        }

        order = orderRepository.save(order);
        bucket.clear();

        logger.info("Order with id {} successfully created", order.uuid());

        return order;
    }

    @Override
    public Order findOrderById(UUID uuid, User user) {
        Order order = orderByIdOrThrow(uuid);

        if (!order.userUuid().equals(user.getId())
            && !user.hasRole(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to get order");
        }

        return order;
    }

    @Override
    public Order deleteOrder(UUID uuid) {
        Order order = orderByIdOrThrow(uuid);

        orderRepository.delete(order);

        return order;
    }

    @Override
    public List<Order> findOrdersByUserId(UUID uuid, User user) {
        if (!user.getId().equals(uuid)
            && !user.hasRole(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to get orders");
        }
        userRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("User with uuid %s doesn't exist", uuid)
                ));

        return orderRepository.findByUserId(uuid);
    }

    @Override
    public UUID completeOrder(UUID uuid, User user) {
        Order order = orderByIdOrThrow(uuid);

        if (!order.userUuid().equals(user.getId())
            && !user.hasRole(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to complete order");
        }

        orderRepository.updateOrderStatusById(uuid, OrderStatus.COMPLETED);
        logger.info("Order with id {} successfully completed", uuid);

        return uuid;
    }

    @Override
    public UUID cancelOrder(UUID uuid, User user) {
        Order order = orderByIdOrThrow(uuid);

        if (!order.userUuid().equals(user.getId())
            && !user.hasRole(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to cancel order");
        }

        orderRepository.updateOrderStatusById(uuid, OrderStatus.CANCELED);
        logger.info("Order with id {} successfully canceled", uuid);

        return uuid;
    }

    private Order orderByIdOrThrow(UUID uuid) {
        return orderRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("Order with uuid %s doesn't exist", uuid)
                ));
    }

    private Map<Product, Integer> extractBucketProducts(Bucket bucket) {
        return bucket.products().entrySet().stream()
                .map(entry -> Map.entry(productRepository.findById(entry.getKey()), entry.getValue()))
                .filter(entry -> entry.getKey().isPresent())
                .map(entry -> Map.entry(entry.getKey().get(), entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

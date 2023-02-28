package com.konstantion.order;

import com.konstantion.bucket.Bucket;
import com.konstantion.exceptions.ValidationException;
import com.konstantion.order.validator.OrderValidator;
import com.konstantion.product.Product;
import com.konstantion.user.User;
import com.konstantion.utils.validator.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CliOrderServiceTest {
    @Mock
    private OrderValidator orderValidator;

    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    private User user;

    private Bucket bucket;

    @BeforeEach
    void setUp() {
        orderService = new CliOrderService(orderRepository, orderValidator);
        user = new User(1L, UUID.randomUUID(), "name", "gmail", "password");
        bucket = new Bucket();
    }


    @Test
    void process_shouldThrowValidationException_whenCreateOrder_withInvalidBucket() {
        doReturn(ValidationResult.of(List.of(new FieldError("myError", "myError", "myError"))))
                .when(orderValidator).isOrderValid(any(Order.class));

        assertThrows(ValidationException.class,
                () -> orderService.createOrder(user, bucket));
    }

    @Test
    void process_shouldCallRepositorySave_whenCreateOrder_withValidOrder() {
        Product bread = Product.builder()
                .name("Bread")
                .price(25.0)
                .build();
        Bucket bucket = new Bucket(user, new HashMap<>(Map.of(bread, 1)));
        Order returnedOrder = Order.builder()
                .userUuid(user.uuid())
                .products(bucket.products())
                .build();

        doReturn(ValidationResult.empty())
                .when(orderValidator).isOrderValid(any(Order.class));

        doReturn(returnedOrder).when(orderRepository).save(any(Order.class));

        orderService.createOrder(user, bucket);

        verify(orderValidator, times(1)).isOrderValid(any(Order.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

}
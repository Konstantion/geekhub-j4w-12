package com.konstantion.order;

import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.utils.ExceptionUtils;
import com.konstantion.order.dto.OrderDto;
import com.konstantion.product.ProductService;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.table.TableService;
import com.konstantion.table.dto.TableDto;
import com.konstantion.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionMessages.NOT_ENOUGH_AUTHORITIES;
import static com.konstantion.user.Permission.*;
import static java.lang.Boolean.FALSE;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public record OrderServiceImpl(
        TableService tableService,
        ProductService productService,
        OrderRepository orderRepository
) implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private static final OrderMapper orderMapper = OrderMapper.INSTANCE;

    @Override
    public OrderDto getById(UUID id) {
        return orderMapper.toDto(getByIdOrThrow(id));
    }

    @Override
    public OrderDto getTableOrder(UUID tableId, User user) {
        if (user.hasNoPermission(GET_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        TableDto table = tableService.getById(tableId, user);

        if (FALSE.equals(table.active())) {
            throw new BadRequestException(format("Table with id %s isn't active", tableId));
        }

        if (isNull(table.orderId())) {
            return null;
        }

        Order order = getByIdOrThrow(table.orderId());

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto transferToAnotherTable(UUID orderId, UUID tableId, User user) {
        if (user.hasNoPermission(TRANSFER_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Order order = getByIdOrThrow(orderId);
        ExceptionUtils.isActiveOrThrow(order);

        TableDto table = tableService.getById(tableId, user);
        ExceptionUtils.isActiveOrThrow(table);

        if (table.hasOrder()) {
            throw new BadRequestException(format("Table with id %s, already has active order", table.id()));
        }

        order.setTableId(table.id());
        orderRepository.save(order);

        tableService.setOrder(table.id(), orderId, user);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto open(UUID tableId, User user) {
        if (user.hasNoPermission(OPEN_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        TableDto table = tableService.getById(tableId, user);
        ExceptionUtils.isActiveOrThrow(table);

        if (nonNull(table.orderId())) {
            throw new BadRequestException(format("Table with id %s already has order", table.orderId()));
        }

        Order order = Order.builder()
                .userId(user.getId())
                .tableId(table.id())
                .productsId(new ArrayList<>())
                .createdAt(now())
                .build();


        orderRepository.save(order);
        tableService.setOrder(table.id(), order.getId(), user);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto close(UUID orderId, User user) {
        if (user.hasNoPermission(CLOSE_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Order order = getByIdOrThrow(orderId);
        ExceptionUtils.isActiveOrThrow(order);

        tableService.clearOrder(order.getTableId(), user);
        prepareToClose(order);

        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto addProduct(UUID orderId, UUID productId, Integer quantity, User user) {
        if (user.hasNoPermission(ADD_PRODUCT_TO_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Order order = getByIdOrThrow(orderId);
        ExceptionUtils.isActiveOrThrow(order);


        ProductDto product = productService.getById(productId);
        ExceptionUtils.isActiveOrThrow(product);

        if (quantity <= 0) {
            throw new BadRequestException(format("Quantity should be greater then 0, given %s", quantity));
        }

        for (int i = 0; i < quantity; i++) {
            order.getProductsId().add(product.id());
        }

        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto removeProduct(UUID orderId, UUID productId, Integer quantity, User user) {
        if (user.hasNoPermission(DELETE_PRODUCT_FROM_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Order order = getByIdOrThrow(orderId);
        ExceptionUtils.isActiveOrThrow(order);


        ProductDto product = productService.getById(productId);
        ExceptionUtils.isActiveOrThrow(product);

        if (quantity <= 0) {
            throw new BadRequestException(format("Quantity should be greater then 0, given %s", quantity));
        }

        for (int i = 0; i < quantity; i++) {
            if (!order.getProductsId().remove(productId)) {
                break;
            }
        }

        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    private Order getByIdOrThrow(UUID id) {
        return orderRepository.findById(id).orElseThrow(() -> {
            throw new BadRequestException(format("Order with id %s doesn't exist", id));
        });
    }

    private void prepareToClose(Order order) {
        order.setClosedAt(now());
        order.setActive(false);
    }
}

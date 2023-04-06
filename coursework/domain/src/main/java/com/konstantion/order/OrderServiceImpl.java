package com.konstantion.order;

import com.konstantion.bill.Bill;
import com.konstantion.bill.BillPort;
import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.utils.ExceptionUtils;
import com.konstantion.product.Product;
import com.konstantion.product.ProductPort;
import com.konstantion.table.Table;
import com.konstantion.table.TablePort;
import com.konstantion.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionMessages.NOT_ENOUGH_AUTHORITIES;
import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;
import static com.konstantion.user.Permission.*;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Component
public record OrderServiceImpl(
        TablePort tablePort,
        ProductPort productPort,
        OrderPort orderPort,
        BillPort billPort
) implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Override
    public Order getById(UUID id) {
        return getByIdOrThrow(id);
    }

    @Override
    public Order getTableOrder(UUID tableId, User user) {
        if (user.hasNoPermission(GET_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Table table = tablePort.findById(tableId)
                .orElseThrow(nonExistingIdSupplier(Table.class, tableId));

        ExceptionUtils.isActiveOrThrow(table);

        if (!table.hasOrder()) {
            return null;
        }

        return orderPort.findById(table.getOrderId())
                .orElseThrow(nonExistingIdSupplier(Order.class, table.getOrderId()));
    }

    @Override
    public Order transferToAnotherTable(UUID orderId, UUID tableId, User user) {
        if (user.hasNoPermission(TRANSFER_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Order order = getByIdOrThrow(orderId);
        ExceptionUtils.isActiveOrThrow(order);

        Table table = tablePort.findById(tableId)
                .orElseThrow(nonExistingIdSupplier(Table.class, tableId));
        ExceptionUtils.isActiveOrThrow(table);

        if (table.hasOrder()) {
            throw new BadRequestException(format("Table with id %s, already has active order with id %s", table.getId(), table.getOrderId()));
        }

        order.setTableId(table.getId());
        table.setOrderId(order.getId());

        orderPort.save(order);
        tablePort.save(table);

        return order;
    }

    @Override
    public Order open(UUID tableId, User user) {
        if (user.hasNoPermission(OPEN_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Table table = tablePort.findById(tableId)
                .orElseThrow(nonExistingIdSupplier(Table.class, tableId));
        ExceptionUtils.isActiveOrThrow(table);

        if (table.hasOrder()) {
            throw new BadRequestException(format("Table with id %s already has order with id %s", table.getId(), table.getOrderId()));
        }

        Order order = Order.builder()
                .userId(user.getId())
                .tableId(table.getId())
                .productsId(new ArrayList<>())
                .createdAt(now())
                .build();


        orderPort.save(order);

        table.setOrderId(order.getId());
        tablePort.save(table);

        return order;
    }

    @Override
    public Order close(UUID orderId, User user) {
        if (user.hasNoPermission(CLOSE_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Order order = getByIdOrThrow(orderId);

        ExceptionUtils.isActiveOrThrow(order);
        if (!order.hasBill()) {
            throw new BadRequestException(format("Order with id %s doesn't have a bill", order.getId()));
        }

        Bill bill = billPort.findById(order.getBillId())
                .orElseThrow(nonExistingIdSupplier(Bill.class, order.getBillId()));
        if (bill.isActive()) {
            throw new BadRequestException(format("Order with id %s has a bill with id %s but it has not been payed", order.getId(), bill.getId()));
        }

        Table table = tablePort.findById(order.getTableId())
                .orElseThrow(nonExistingIdSupplier(Table.class, order.getTableId()));
        table.removeOrder();

        prepareToClose(order);

        tablePort.save(table);
        orderPort.save(order);

        return order;
    }

    @Override
    public Order addProduct(UUID orderId, UUID productId, Integer quantity, User user) {
        if (user.hasNoPermission(ADD_PRODUCT_TO_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Order order = getByIdOrThrow(orderId);
        ExceptionUtils.isActiveOrThrow(order);


        Product product = productPort.findById(productId)
                .orElseThrow(nonExistingIdSupplier(Product.class, productId));
        ExceptionUtils.isActiveOrThrow(product);

        if (quantity <= 0) {
            throw new BadRequestException(format("Quantity should be greater then 0, given %s", quantity));
        }

        for (int i = 0; i < quantity; i++) {
            order.getProductsId().add(product.getId());
        }

        orderPort.save(order);

        return order;
    }

    @Override
    public Order removeProduct(UUID orderId, UUID productId, Integer quantity, User user) {
        if (user.hasNoPermission(DELETE_PRODUCT_FROM_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Order order = getByIdOrThrow(orderId);
        ExceptionUtils.isActiveOrThrow(order);


        Product product = productPort.findById(productId)
                .orElseThrow(nonExistingIdSupplier(Product.class, productId));
        ExceptionUtils.isActiveOrThrow(product);

        if (quantity <= 0) {
            throw new BadRequestException(format("Quantity should be greater then 0, given %s", quantity));
        }

        for (int i = 0; i < quantity; i++) {
            if (!order.getProductsId().remove(product.getId())) {
                break;
            }
        }

        orderPort.save(order);

        return order;
    }

    private Order getByIdOrThrow(UUID id) {
        return orderPort.findById(id).orElseThrow(() -> {
            throw new BadRequestException(format("Order with id %s doesn't exist", id));
        });
    }

    private void prepareToClose(Order order) {
        order.setClosedAt(now());
        order.setActive(false);
    }
}

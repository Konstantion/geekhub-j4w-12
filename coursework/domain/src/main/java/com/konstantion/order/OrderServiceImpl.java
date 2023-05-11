package com.konstantion.order;

import com.konstantion.bill.Bill;
import com.konstantion.bill.BillPort;
import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.utils.ExceptionUtils;
import com.konstantion.order.model.OrderProductsRequest;
import com.konstantion.product.Product;
import com.konstantion.product.ProductPort;
import com.konstantion.table.Table;
import com.konstantion.table.TablePort;
import com.konstantion.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.konstantion.exception.utils.ExceptionMessages.NOT_ENOUGH_AUTHORITIES;
import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;
import static com.konstantion.user.Permission.*;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;

@Component
public record OrderServiceImpl(
        TablePort tablePort,
        ProductPort productPort,
        OrderPort orderPort,
        BillPort billPort
) implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Override
    public List<Order> getAll(boolean onlyActive) {
        List<Order> orders = orderPort.findAll();
        if (onlyActive) {
            return orders.stream().filter(Order::isActive).toList();
        }
        logger.info("All orders successfully returned");
        return orders;
    }

    @Override
    public Order getById(UUID id) {
        Order order = getByIdOrThrow(id);
        logger.info("Order with id {} successfully returned", id);
        return order;
    }

    @Override
    public Order transferToAnotherTable(UUID orderId, UUID tableId, User user) {
        if (user.hasNoPermission(TRANSFER_ORDER)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Order order = getByIdOrThrow(orderId);
        ExceptionUtils.isActiveOrThrow(order);

        Table newTable = tablePort.findById(tableId)
                .orElseThrow(nonExistingIdSupplier(Table.class, tableId));
        ExceptionUtils.isActiveOrThrow(newTable);

        if (newTable.hasOrder()) {
            throw new BadRequestException(format("Table with id %s, already has active order with id %s", newTable.getId(), newTable.getOrderId()));
        }

        if (nonNull(order.getTableId())) {
            Table oldTable = tablePort().findById(order.getTableId())
                    .orElseThrow(nonExistingIdSupplier(Table.class, order.getTableId()));
            oldTable.removeOrder();
            tablePort.save(oldTable);
        }

        order.setTableId(newTable.getId());
        newTable.setOrderId(order.getId());

        orderPort.save(order);
        tablePort.save(newTable);

        logger.info("Order with id {} successfully transferred to the table with id {}", orderId, tableId);
        return order;
    }

    @Override
    public Order open(UUID tableId, User user) {
        if (user.hasNoPermission(OPEN_ORDER)
            && user.hasNoPermission(SUPER_USER)) {
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
                .active(true)
                .build();


        orderPort.save(order);

        table.setOrderId(order.getId());
        tablePort.save(table);

        return order;
    }

    @Override
    public Order close(UUID orderId, User user) {
        if (user.hasNoPermission(CLOSE_ORDER)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Order order = getByIdOrThrow(orderId);
        ExceptionUtils.isActiveOrThrow(order);

        if (order.getProductsId().isEmpty()) {
            orderPort.delete(order);
            logger.warn("Order with id {} didn't contain any product, so it was successfully deleted and returned", orderId);
            return order;
        }

        if (!order.hasBill()) {
            throw new BadRequestException(format("Order with id %s doesn't have a bill", order.getId()));
        }

        Bill bill = billPort.findById(order.getBillId())
                .orElseThrow(nonExistingIdSupplier(Bill.class, order.getBillId()));
        if (bill.isActive()) {
            throw new BadRequestException(format("Order with id %s has a bill with id %s that has not been payed", order.getId(), bill.getId()));
        }

        if (nonNull(order.getTableId())) {
            Table table = tablePort.findById(order.getTableId())
                    .orElseThrow(nonExistingIdSupplier(Table.class, order.getTableId()));
            table.removeOrder();
            tablePort.save(table);
        }

        prepareToClose(order);
        orderPort.save(order);

        logger.info("Order with id {} successfully closed and returned", order);
        return order;
    }

    @Override
    public Order delete(UUID orderId, User user) {
        if (user.hasNoPermission(DELETE_ORDER)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Order order = getByIdOrThrow(orderId);

        orderPort.delete(order);

        logger.info("Order with id {} successfully deleted and returned", orderId);
        return order;
    }

    /**
     * Fast method to get order products <b>without image bytes</b>!
     * <p> n - products</p>
     * <p> k - unique products | k є o(n)</p>
     * <p> db(x) - fetch image from database time;</p>
     * <p> db(1) + Θ(n) + Θ(n + db(k)) + Θ(n) = O(n + db(k))</p>
     */
    @Override
    public List<Product> getProductsByOrderId(UUID orderId) {
        Order order = getByIdOrThrow(orderId);
        List<UUID> productIds = order.getProductsId();
        Map<UUID, Product> productsMap = new HashSet<>(productIds).stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> {
                            Product product = productPort.findById(id).orElseThrow();
                            product.setImageBytes(null);
                            return product;
                        }
                ));
        List<Product> products = productIds.stream()
                .map(productsMap::get)
                .toList();
        logger.info("Products from order with id {} successfully returned", orderId);
        return products;
    }

    @Override
    public int addProduct(UUID orderId, OrderProductsRequest request, User user) {
        if (user.hasNoPermission(ADD_PRODUCT_TO_ORDER)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        UUID productId = request.productId();
        int quantity = request.quantity();

        Order order = getByIdOrThrow(orderId);
        ExceptionUtils.isActiveOrThrow(order);

        if (order.hasBill()) {
            throw new BadRequestException(format("Products can't be added to the order with id %s because it already has a bill with id %s", order.getId(), order.getBillId()));
        }

        Product product = productPort.findById(productId)
                .orElseThrow(nonExistingIdSupplier(Product.class, productId));
        ExceptionUtils.isActiveOrThrow(product);

        if (quantity <= 0) {
            throw new BadRequestException(format("Quantity should be greater than 0, given %s", quantity));
        }
        long theoreticalQuantity = order.getProductsId().stream().filter(id -> id.equals(product.getId())).count() + quantity;
        if (theoreticalQuantity > 1 << 10) {
            throw new BadRequestException(format("Total quantity of product in order should not be greater than 1024, given %s", theoreticalQuantity));
        }

        int counter = 0;
        for (; counter < quantity; counter++) {
            order.getProductsId().add(product.getId());
        }

        orderPort.save(order);
        logger.info("{} product(s) with id {} successfully added to the order with id {}", counter, productId, orderId);
        return counter;
    }

    @Override
    public int removeProduct(UUID orderId, OrderProductsRequest request, User user) {
        if (user.hasNoPermission(DELETE_PRODUCT_FROM_ORDER)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        UUID productId = request.productId();
        int quantity = request.quantity();

        Order order = getByIdOrThrow(orderId);
        ExceptionUtils.isActiveOrThrow(order);

        if (order.hasBill()) {
            throw new BadRequestException(format("Products can't be removed from the order with id %s because it already has a bill with id %s", order.getId(), order.getBillId()));
        }

        Product product = productPort.findById(productId)
                .orElseThrow(nonExistingIdSupplier(Product.class, productId));

        if (quantity <= 0) {
            throw new BadRequestException(format("Quantity should be greater than 0, given %s", quantity));
        }
        int counter = 0;
        for (; counter < quantity; counter++) {
            if (!order.getProductsId().remove(product.getId())) {
                break;
            }
        }
        orderPort.save(order);
        logger.info("{} product(s) with id {} successfully removed from the order with id {}", counter, productId, orderId);
        return counter;
    }

    private Order getByIdOrThrow(UUID id) {
        return orderPort.findById(id)
                .orElseThrow(nonExistingIdSupplier(Order.class, id));
    }

    private void prepareToClose(Order order) {
        order.setClosedAt(now());
        order.setActive(false);
    }
}

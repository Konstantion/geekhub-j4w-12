package com.konstantion.bill;

import com.konstantion.bill.model.CreateBillRequest;
import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.utils.ExceptionUtils;
import com.konstantion.guest.Guest;
import com.konstantion.guest.GuestPort;
import com.konstantion.order.Order;
import com.konstantion.order.OrderPort;
import com.konstantion.product.Product;
import com.konstantion.product.ProductPort;
import com.konstantion.table.TablePort;
import com.konstantion.user.User;
import com.konstantion.utils.DoubleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionMessages.NOT_ENOUGH_AUTHORITIES;
import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;
import static com.konstantion.user.Permission.*;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;

@Component
public record BillServiceImpl(
        OrderPort orderPort,
        TablePort tablePort,
        GuestPort guestPort,
        ProductPort productPort,
        BillPort billPort
) implements BillService {
    private static final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

    @Override
    public List<Bill> getAll(boolean onlyActive) {
        List<Bill> bills = billPort.findAll();

        if (onlyActive) {
            return bills.stream().filter(Bill::isActive).toList();
        }
        logger.info("Bills successfully returned");
        return bills;
    }

    @Override
    public Bill getById(UUID id) {
        Bill bill = getByIdOrThrow(id);
        logger.info("Bill with id {} successfully returned", id);
        return bill;
    }

    @Override
    public Bill create(CreateBillRequest createBillRequest, User user) {
        if (user.hasNoPermission(CREATE_BILL_FROM_ORDER)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Order order = orderPort.findById(createBillRequest.orderId())
                .orElseThrow(nonExistingIdSupplier(Order.class, createBillRequest.orderId()));
        ExceptionUtils.isActiveOrThrow(order);

        if (order.hasBill()) {
            throw new BadRequestException(format("Order with id %s already has bill", order.getId()));
        }

        if (order.getProductsId().isEmpty()) {
            throw new BadRequestException(format("Cannot create bill for order with id %s because it doesn't contain any product", order.getId()));
        }

        Guest guest = guestOrNull(createBillRequest.guestId());

        Double price = calculateOrderPrice(order);
        Double priceWithDiscount = calculatePriceWithDiscount(price, guest);

        Bill bill = Bill.builder()
                .waiterId(user.getId())
                .orderId(order.getId())
                .guestId(createBillRequest.guestId())
                .price(price)
                .priceWithDiscount(priceWithDiscount)
                .active(true)
                .createdAt(now())
                .build();

        billPort.save(bill);

        order.setBillId(bill.getId());
        orderPort.save(order);

        logger.info("Bill successfully created and returned");
        return bill;
    }

    @Override
    public Bill cancel(UUID billId, User user) {
        if (user.hasNoPermission(CANCEL_BILL)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Bill bill = getByIdOrThrow(billId);

        Order order = orderPort.findById(bill.getOrderId())
                .orElseThrow(nonExistingIdSupplier(Order.class, bill.getOrderId()));

        order.removeBill();

        orderPort.save(order);
        billPort.delete(bill);

        logger.info("Bill wth id {} successfully canceled and returned", billId);
        return bill;
    }

    @Override
    public Bill close(UUID billId, User user) {
        if (user.hasNoPermission(CLOSE_BILL)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Bill bill = getByIdOrThrow(billId);
        if (!bill.isActive()) {
            logger.warn("Bill with id {} is already closed and returned", billId);
            return bill;
        }

        prepareToClose(bill);

        billPort.save(bill);
        logger.info("Bill with id {} successfully canceled and returned", billId);
        return bill;
    }

    @Override
    public Bill activate(UUID billId, User user) {
        if (user.hasNoPermission(CHANGE_BILL_STATE)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Bill bill = getByIdOrThrow(billId);

        if (bill.isActive()) {
            logger.warn("Bill with id {} is already active and returned", billId);
            return bill;
        }

        prepareToActivate(bill);

        billPort.save(bill);

        logger.info("Bill with id {} successfully activated and returned", billId);
        return bill;
    }

    private Bill getByIdOrThrow(UUID id) {
        return billPort.findById(id)
                .orElseThrow(nonExistingIdSupplier(Bill.class, id));
    }

    private Double calculateOrderPrice(Order order) {
        Double orderPrice = order.getProductsId().stream()
                .map(productPort::findById)
                .map(Optional::orElseThrow)
                .map(Product::getPrice)
                .reduce(0.0, Double::sum);

        return DoubleUtils.round(orderPrice, 2);
    }

    /**
     * Method null save for parameter <b style='color: red'>guest</b>
     */
    private Double calculatePriceWithDiscount(Double orderPrice, @Nullable Guest guest) {
        double discountPercent = 0.0;
        if (nonNull(guest)) {
            discountPercent = max(0.0, min(guest.getDiscountPercent(), 100.0));
        }
        double discountedPrice = orderPrice - DoubleUtils.percent(orderPrice, discountPercent);

        return DoubleUtils.round(discountedPrice, 2);
    }

    private void prepareToClose(Bill bill) {
        bill.setClosedAt(now());
        bill.setActive(false);
    }

    private void prepareToActivate(Bill bill) {
        bill.setClosedAt(null);
        bill.setActive(true);
    }


    private Guest guestOrNull(UUID guestId) {
        boolean guestPresent = guestId != null;
        Guest guest = null;
        if (guestPresent) {
            guest = guestPort.findById(guestId)
                    .orElseThrow(nonExistingIdSupplier(Guest.class, guestId));
            ExceptionUtils.isActiveOrThrow(guest);
        }
        return guest;
    }
}

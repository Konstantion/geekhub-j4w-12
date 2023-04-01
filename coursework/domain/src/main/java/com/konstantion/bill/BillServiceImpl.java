package com.konstantion.bill;

import com.konstantion.bill.dto.BillDto;
import com.konstantion.bill.dto.CreationBillDto;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.utils.ExceptionUtils;
import com.konstantion.guest.GuestService;
import com.konstantion.guest.dto.GuestDto;
import com.konstantion.order.OrderService;
import com.konstantion.order.dto.OrderDto;
import com.konstantion.product.ProductService;
import com.konstantion.product.dto.ProductDto;
import com.konstantion.table.TableService;
import com.konstantion.user.User;
import com.konstantion.utils.DoubleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionMessages.NOT_ENOUGH_AUTHORITIES;
import static com.konstantion.user.Permission.CREATE_BILL_FROM_ORDER;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.time.LocalDateTime.now;
import static java.util.Objects.nonNull;

@Component
public record BillServiceImpl(
        OrderService orderService,
        TableService tableService,
        GuestService guestService,
        ProductService productService,
        BillRepository billRepository
) implements BillService {
    private static final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);
    private static final BillMapper billMapper = BillMapper.INSTANCE;

    @Override
    public BillDto create(CreationBillDto cbdto, User user) {
        if (user.hasNoPermission(CREATE_BILL_FROM_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        OrderDto order = orderService.getById(cbdto.orderId());
        ExceptionUtils.isActiveOrThrow(order);

        boolean guestPresent = cbdto.guestId() != null;
        GuestDto guest = null;
        if (guestPresent) {
            guest = guestService.getById(cbdto.guestId());
            ExceptionUtils.isActiveOrThrow(guest);
        }

        Double price = calculateOrderPrice(order);
        Double priceWithDiscount = calculateOrderPriceWithDiscount(order, guest);

        Bill bill = Bill.builder()
                .productsId(order.productsId())
                .waiterId(user.getId())
                .orderId(order.id())
                .guestId(cbdto.guestId())
                .price(price)
                .priceWithDiscount(priceWithDiscount)
                .active(true)
                .createdAt(now())
                .build();

        billRepository.save(bill);

        return billMapper.toDto(bill);
    }


    @Override
    public BillDto close(UUID billId, User user) {
        return null;
    }

    @Override
    public BillDto restore(UUID billId, UUID tableId, User user) {
        return null;
    }

    @Override
    public BillDto activate(UUID billId, User user) {
        return null;
    }

    @Override
    public BillDto deactivate(UUID billId, User user) {
        return null;
    }

    private Double calculateOrderPrice(OrderDto order) {
        Double orderPrice = order.productsId().stream()
                .map(productService::getById)
                .map(ProductDto::price)
                .reduce(0.0, Double::sum);

        return DoubleUtils.round(orderPrice, 2);
    }

    /**
     * Method null save for parameter <b style='color: red'>guest</b>
     */
    private Double calculateOrderPriceWithDiscount(OrderDto order, @Nullable GuestDto guest) {
        Double orderPrice = calculateOrderPrice(order);
        double discountPercent = 0.0;
        if (nonNull(guest)) {
            discountPercent = max(0.0, min(guest.discountPercent(), 100.0));
        }
        double discountPrice = DoubleUtils.percent(orderPrice, discountPercent);

        return DoubleUtils.round(discountPrice, 2);
    }
}

package com.konstantion.bill;

import com.konstantion.bill.model.CreateBillRequest;
import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.NonExistingIdException;
import com.konstantion.guest.Guest;
import com.konstantion.guest.GuestPort;
import com.konstantion.order.Order;
import com.konstantion.order.OrderPort;
import com.konstantion.product.Product;
import com.konstantion.product.ProductPort;
import com.konstantion.table.TablePort;
import com.konstantion.user.Permission;
import com.konstantion.user.User;
import com.konstantion.user.UserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BillServiceImplTest {
    @Mock
    OrderPort orderPort;
    @Mock
    TablePort tablePort;
    @Mock
    UserPort userPort;
    @Mock
    GuestPort guestPort;
    @Mock
    ProductPort productPort;
    @Mock
    BillPort billPort;
    @Mock
    User user;
    @Mock
    Product bread;
    @Mock
    Product water;
    @InjectMocks
    BillServiceImpl billService;
    List<Bill> bills;
    Bill activeBill;
    Bill inactiveBill;
    UUID activeBillId;
    UUID inactiveBillId;
    UUID orderId;
    UUID guestId;
    CreateBillRequest request;

    @BeforeEach
    void setUp() {
        activeBillId = UUID.randomUUID();
        inactiveBillId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        guestId = UUID.randomUUID();

        request = new CreateBillRequest(orderId, guestId);


        activeBill = Bill.builder()
                .id(activeBillId)
                .active(true)
                .price(123.0)
                .build();

        inactiveBill = Bill.builder()
                .id(inactiveBillId)
                .active(false)
                .price(321.0)
                .build();

        bills = List.of(activeBill, inactiveBill);
    }

    @Test
    void shouldReturnBillsWhenGetBill() {
        when(billPort.findAll()).thenReturn(bills);

        List<Bill> activeBills = billService.getAll();
        List<Bill> allBills = billService.getAll(false);

        assertThat(activeBills).contains(activeBill);
        assertThat(allBills).contains(activeBill, inactiveBill);

        verify(billPort, times(2)).findAll();
    }

    @Test
    void shouldReturnBillWhenGetByIdExistingId() {
        when(billPort.findById(activeBillId)).thenReturn(Optional.of(activeBill));

        Bill actualBill = billService.getById(activeBillId);

        assertThat(actualBill).isEqualTo(activeBill);
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenGetByIdExistingId() {
        when(billPort.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billService.getById(activeBillId))
                .isInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldThrowForbiddenExceptionWhenCreateWithoutPermission() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(true);

        assertThatThrownBy(() -> billService.create(request, user))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenCreateWithNonExistingOrder() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(orderPort.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billService.create(request, user))
                .isInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenCreateWithInactiveOrder() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(orderPort.findById(any(UUID.class))).thenReturn(Optional.of(Order.builder().active(false).build()));

        assertThatThrownBy(() -> billService.create(request, user))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenCreateWithOrderThatAlreadyHasBill() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(orderPort.findById(any(UUID.class))).thenReturn(Optional.of(Order.builder().active(true).billId(UUID.randomUUID()).build()));

        assertThatThrownBy(() -> billService.create(request, user))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenCreateWithOrderThatDoesNotContainProducts() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(orderPort.findById(any(UUID.class))).thenReturn(Optional.of(
                Order.builder()
                        .active(true)
                        .billId(null)
                        .productsId(Collections.emptyList())
                        .build()
        ));

        assertThatThrownBy(() -> billService.create(request, user))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldCreateBillWhenCreateWithoutGuestId() {
        request = new CreateBillRequest(UUID.randomUUID(), null);

        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(orderPort.findById(any(UUID.class))).thenReturn(Optional.of(
                Order.builder()
                        .active(true)
                        .billId(null)
                        .productsId(List.of(UUID.randomUUID(), UUID.randomUUID()))
                        .build()
        ));
        when(productPort.findById(any(UUID.class)))
                .thenReturn(Optional.of(bread))
                .thenReturn(Optional.of(water));
        when(billPort.save(any(Bill.class))).thenReturn(Bill.builder().id(UUID.randomUUID()).build());
        when(bread.getPrice()).thenReturn(50.333);
        when(water.getPrice()).thenReturn(50.114);

        Bill bill = billService.create(request, user);

        assertThat(bill.getPrice()).isEqualTo(100.45);
        assertThat(bill.getPrice()).isEqualTo(bill.getPriceWithDiscount());
    }

    @Test
    void shouldCreateBillWhenCreateWithGuestId() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(orderPort.findById(any(UUID.class))).thenReturn(Optional.of(
                Order.builder()
                        .active(true)
                        .billId(null)
                        .productsId(List.of(UUID.randomUUID(), UUID.randomUUID()))
                        .build()
        ));
        when(productPort.findById(any(UUID.class)))
                .thenReturn(Optional.of(bread))
                .thenReturn(Optional.of(water));
        when(billPort.save(any(Bill.class))).thenReturn(Bill.builder().id(UUID.randomUUID()).build());
        when(guestPort.findById(guestId)).thenReturn(Optional.of(Guest.builder().active(true).discountPercent(10.0).build()));
        when(bread.getPrice()).thenReturn(51.002);
        when(water.getPrice()).thenReturn(49.002);

        Bill bill = billService.create(request, user);

        assertThat(bill.getPrice()).isEqualTo(100.0);
        assertThat(bill.getPriceWithDiscount()).isEqualTo(90.0);
    }

    @Test
    void shouldThrowForbiddenExceptionWhenCancelWithoutPermission() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(true);

        assertThatThrownBy(() -> billService.cancel(activeBillId, user))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenCancelWithNonExistingBill() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(billPort.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billService.cancel(activeBillId, user))
                .isInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenCancelBillOrderDoesNotExist() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(billPort.findById(any(UUID.class))).thenReturn(Optional.of(Bill.builder().active(true).orderId(UUID.randomUUID()).build()));
        when(orderPort.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billService.cancel(activeBillId, user))
                .isInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldDeleteBillWhenCancelValidBill() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(billPort.findById(any(UUID.class))).thenReturn(Optional.of(Bill.builder().active(true).orderId(UUID.randomUUID()).build()));
        when(orderPort.findById(any(UUID.class))).thenReturn(Optional.of(Order.builder().active(true).build()));

        Bill bill = billService.cancel(activeBillId, user);

        verify(billPort, times(1)).delete(bill);
    }

    @Test
    void shouldThrowForbiddenExceptionWhenCloseWithoutPermission() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(true);

        assertThatThrownBy(() -> billService.close(activeBillId, user))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenCloseWithNonExistingBill() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(billPort.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billService.cancel(activeBillId, user))
                .isInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldDeactivateBillWhenCloseWithValidBill() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(billPort.findById(any(UUID.class)))
                .thenReturn(Optional.of(activeBill))
                .thenReturn(Optional.of(inactiveBill));
        when(billPort.save(any(Bill.class))).thenReturn(null);

        Bill deactivatedBill = billService.close(activeBillId, user);
        Bill inactiveBill = billService.close(inactiveBillId, user);

        verify(billPort, times(1)).save(activeBill);
        assertThat(deactivatedBill.isActive()).isFalse();
        assertThat(inactiveBill.isActive()).isFalse();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenActivateWithoutPermission() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(true);

        assertThatThrownBy(() -> billService.activate(activeBillId, user))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenActivateWithNonExistingBill() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(billPort.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> billService.activate(activeBillId, user))
                .isInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldDeactivateBillWhenActivateWithValidBill() {
        when(orderPort.findById(any())).thenReturn(Optional.of(Order.builder().active(false).build()));
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(billPort.findById(any(UUID.class)))
                .thenReturn(Optional.of(activeBill))
                .thenReturn(Optional.of(inactiveBill));
        when(billPort.save(any(Bill.class))).thenReturn(null);

        Bill activatedBill = billService.activate(inactiveBillId, user);
        Bill activeBill = billService.activate(activeBillId, user);

        verify(billPort, times(1)).save(inactiveBill);
        assertThat(activatedBill.isActive()).isTrue();
        assertThat(activeBill.isActive()).isTrue();
    }
}
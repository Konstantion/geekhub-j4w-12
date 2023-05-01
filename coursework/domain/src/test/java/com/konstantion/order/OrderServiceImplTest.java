package com.konstantion.order;

import com.google.common.collect.Lists;
import com.konstantion.bill.Bill;
import com.konstantion.bill.BillPort;
import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.NonExistingIdException;
import com.konstantion.order.model.OrderProductsRequest;
import com.konstantion.product.Product;
import com.konstantion.product.ProductPort;
import com.konstantion.table.Table;
import com.konstantion.table.TablePort;
import com.konstantion.user.Permission;
import com.konstantion.user.User;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class OrderServiceImplTest {
    @Mock
    TablePort tablePort;
    @Mock
    ProductPort productPort;
    @Mock
    OrderPort orderPort;
    @Mock
    BillPort billPort;
    @Mock
    User user;
    @InjectMocks
    OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
    }

    @Test
    void shouldReturnAllOrdersWhenGetAll() {
        when(orderPort.findAll()).thenReturn(List.of(
                Order.builder().active(true).build(),
                Order.builder().active(false).build()
        ));

        List<Order> activeOrders = orderService.getAll(true);
        List<Order> allOrders = orderService.getAll(false);

        assertThat(activeOrders)
                .hasSize(1);
        assertThat(allOrders)
                .hasSize(2);
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenGetByIdWithNonExistingId() {
        when(orderPort.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getById(UUID.randomUUID()))
                .isExactlyInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldReturnOrderWhenGetByIdWithExistingId() {
        when(orderPort.findById(any())).thenReturn(Optional.of(Order.builder().build()));

        Order actual = orderService.getById(UUID.randomUUID());

        assertThat(actual).isNotNull();
    }

    @Test
    void shouldThrowForbiddenExceptionWhenMethodRequirePermissionWithoutPermission() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(true);

        AssertionsForClassTypes.assertThatThrownBy(() -> orderService.close(null, user))
                .isInstanceOf(ForbiddenException.class);

        AssertionsForClassTypes.assertThatThrownBy(() -> orderService.delete(null, user))
                .isInstanceOf(ForbiddenException.class);

        AssertionsForClassTypes.assertThatThrownBy(() -> orderService.open(null, user))
                .isInstanceOf(ForbiddenException.class);

        AssertionsForClassTypes.assertThatThrownBy(() -> orderService.transferToAnotherTable(null, null, user))
                .isInstanceOf(ForbiddenException.class);

        AssertionsForClassTypes.assertThatThrownBy(() -> orderService.addProduct(null, null, user))
                .isInstanceOf(ForbiddenException.class);

        AssertionsForClassTypes.assertThatThrownBy(() -> orderService.removeProduct(null, null, user))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenTransferToAnotherTableWithTableThatHasOrder() {
        when(tablePort.findById(any())).thenReturn(Optional.of(Table.builder().active(true).orderId(UUID.randomUUID()).build()));
        when(orderPort.findById(any())).thenReturn(Optional.of(Order.builder().build()));

        assertThatThrownBy(() -> orderService.transferToAnotherTable(UUID.randomUUID(), UUID.randomUUID(), user))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldTransferOrderWhenTransferToAnotherTableWithTableThatHasOrder() {
        UUID orderId = UUID.randomUUID();
        UUID tableId = UUID.randomUUID();
        Table dbTable = Table.builder().id(tableId).active(true).orderId(null).build();
        when(tablePort.findById(tableId)).thenReturn(Optional.of(dbTable));
        when(orderPort.findById(orderId)).thenReturn(Optional.of(Order.builder().id(orderId).build()));

        Order order = orderService.transferToAnotherTable(orderId, tableId, user);

        assertThat(order)
                .isNotNull()
                .extracting(Order::getTableId)
                .isEqualTo(tableId);

        verify(tablePort, times(1)).save(dbTable);
    }

    @Test
    void shouldThrowBadRequestExceptionOrderWithTableThatHasOrder() {
        when(tablePort.findById(any())).thenReturn(Optional.of(Table.builder().active(true).orderId(UUID.randomUUID()).build()));
        when(orderPort.findById(any())).thenReturn(Optional.of(Order.builder().build()));

        assertThatThrownBy(() -> orderService.open(UUID.randomUUID(), user))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldCreateOrderWhenOpen() {
        UUID tableId = UUID.randomUUID();
        Table dbTable = Table.builder().id(tableId).active(true).orderId(null).build();
        when(tablePort.findById(tableId)).thenReturn(Optional.of(dbTable));

        Order order = orderService.open(tableId, user);

        assertThat(order).isNotNull()
                .extracting(Order::getTableId)
                .isEqualTo(tableId);
        assertThat(order.getCreatedAt())
                .isEqualToIgnoringHours(now());
        assertThat(order.isActive()).isTrue();

        verify(tablePort, times(1)).save(dbTable);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenCloseWithOrderWithoutBill() {
        when(orderPort.findById(any())).thenReturn(Optional.of(
                Order.builder()
                        .productsId(List.of(UUID.randomUUID()))
                        .billId(null)
                        .active(true)
                        .build()
        ));

        assertThatThrownBy(() -> orderService.close(UUID.randomUUID(), user))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenCloseWithOrderWithActiveBill() {
        when(billPort.findById(any())).thenReturn(Optional.of(Bill.builder().active(true).build()));
        when(tablePort.findById(any())).thenReturn(Optional.of(Table.builder().orderId(UUID.randomUUID()).build()));
        when(orderPort.findById(any())).thenReturn(Optional.of(
                Order.builder()
                        .productsId(List.of(UUID.randomUUID()))
                        .billId(UUID.randomUUID())
                        .tableId(UUID.randomUUID())
                        .active(true)
                        .build()
        ));

        assertThatThrownBy(() -> orderService.close(UUID.randomUUID(), user))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldCloseWhenCloseWithValidData() {
        when(billPort.findById(any())).thenReturn(Optional.of(Bill.builder().active(false).build()));
        when(tablePort.findById(any())).thenReturn(Optional.of(Table.builder().orderId(UUID.randomUUID()).build()));
        when(orderPort.findById(any())).thenReturn(Optional.of(
                Order.builder()
                        .productsId(List.of(UUID.randomUUID()))
                        .billId(UUID.randomUUID())
                        .tableId(UUID.randomUUID())
                        .active(true)
                        .build()
        ));

        Order closed = orderService.close(UUID.randomUUID(), user);

        assertThat(closed).isNotNull()
                .extracting(Order::isActive)
                .isEqualTo(false);
        assertThat(closed.getClosedAt()).isNotNull()
                .isEqualToIgnoringHours(now());

        verify(tablePort, times(1)).save(any());
        verify(orderPort, times(1)).save(any());
    }

    @Test
    void shouldDeleteOrderWhenDelete() {
        when(orderPort.findById(any())).thenReturn(Optional.of(Order.builder().build()));

        Order deleted = orderService.delete(UUID.randomUUID(), user);

        assertThat(deleted).isNotNull();
    }

    @Test
    void shouldThrowBadRequestExceptionWhenAddProductOrRemoveProductWithInvalidQuantity() {
        when(orderPort.findById(any())).thenReturn(Optional.of(Order.builder().active(true).productsId(List.of()).build()));
        when(productPort.findById(any())).thenReturn(Optional.of(Product.builder().active(true).build()));
        assertThatThrownBy(() -> orderService.addProduct(UUID.randomUUID(), new OrderProductsRequest(UUID.randomUUID(), -1), user))
                .isExactlyInstanceOf(BadRequestException.class);

        assertThatThrownBy(() -> orderService.removeProduct(UUID.randomUUID(), new OrderProductsRequest(UUID.randomUUID(), -1), user))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldAddProductsWhenAddProductWithValidQuantity() {
        when(orderPort.findById(any())).thenReturn(Optional.of(Order.builder().active(true).productsId(Lists.newArrayList()).build()));
        when(productPort.findById(any())).thenReturn(Optional.of(Product.builder().active(true).build()));
        int counter = orderService.addProduct(UUID.randomUUID(), new OrderProductsRequest(UUID.randomUUID(), 3), user);

        assertThat(counter).isEqualTo(3);

        verify(orderPort, times(1)).save(any());
        verify(productPort, times(1)).findById(any());
    }

    @Test
    void shouldRemoveProductsWhenRemoveProductsWithValidQuantity() {
        UUID productId = UUID.randomUUID();
        Product product = Product.builder().id(productId).active(true).build();
        when(orderPort.findById(any())).thenReturn(Optional.of(Order.builder().active(true).productsId(Lists.newArrayList(productId, productId, productId)).build()));
        when(productPort.findById(productId)).thenReturn(Optional.of(product));

        int counter = orderService.removeProduct(UUID.randomUUID(), new OrderProductsRequest(productId, 10), user);

        assertThat(counter).isEqualTo(3);

        verify(orderPort, times(1)).save(any());
        verify(productPort, times(1)).findById(any());
    }

    @Test
    void shouldThrowBadRequestExceptionWhenAddProductOrRemoveProductWithOrderThatHasBill() {
        when(orderPort.findById(any())).thenReturn(Optional.of(Order.builder().active(true).billId(UUID.randomUUID()).productsId(List.of()).build()));
        when(productPort.findById(any())).thenReturn(Optional.of(Product.builder().active(true).build()));

        assertThatThrownBy(() -> orderService.addProduct(UUID.randomUUID(), new OrderProductsRequest(UUID.randomUUID(), 1), user))
                .isExactlyInstanceOf(BadRequestException.class);

        assertThatThrownBy(() -> orderService.removeProduct(UUID.randomUUID(), new OrderProductsRequest(UUID.randomUUID(), 1), user))
                .isExactlyInstanceOf(BadRequestException.class);
    }
}
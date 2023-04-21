package com.konstantion.table;

import com.google.common.collect.Sets;
import com.konstantion.exception.*;
import com.konstantion.hall.Hall;
import com.konstantion.hall.HallPort;
import com.konstantion.order.Order;
import com.konstantion.order.OrderPort;
import com.konstantion.order.OrderService;
import com.konstantion.table.model.CreateTableRequest;
import com.konstantion.table.model.TableWaitersRequest;
import com.konstantion.table.model.UpdateTableRequest;
import com.konstantion.table.validator.TableValidator;
import com.konstantion.user.Permission;
import com.konstantion.user.User;
import com.konstantion.user.UserPort;
import com.konstantion.utils.validator.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class TableServiceImplTest {
    @Mock
    TableValidator tableValidator;
    @Mock
    TablePort tablePort;
    @Mock
    HallPort hallPort;
    @Mock
    OrderPort orderPort;
    @Mock
    UserPort userPort;
    @Mock
    OrderService orderService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    User user;
    @InjectMocks
    TableServiceImpl tableService;


    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldReturnAllTabledWhenGetAll() {
        when(tablePort.findAll()).thenReturn(List.of(
                Table.builder().active(true).build(),
                Table.builder().active(false).build()
        ));

        List<Table> activeTables = tableService.getAll(true);
        List<Table> tables = tableService.getAll(false);

        assertThat(activeTables)
                .hasSize(1);
        assertThat(tables)
                .hasSize(2);
    }

    @Test
    void shouldThrowForbiddenExceptionWhenMethodRequirePermissionWithoutPermission() {
        when(user.hasNoPermission(any(Permission.class))).thenReturn(true);

        assertThatThrownBy(() -> tableService.activate(null, user))
                .isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(() -> tableService.deactivate(null, user))
                .isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(() -> tableService.create(null, user))
                .isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(() -> tableService.delete(null, user))
                .isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(() -> tableService.update(null, null, user))
                .isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(() -> tableService.addWaiter(null, null, user))
                .isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(() -> tableService.removeWaiter(null, null, user))
                .isInstanceOf(ForbiddenException.class);
        assertThatThrownBy(() -> tableService.removeAllWaiters(null, user))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenGetByIdWithNonExistingId() {
        UUID randomId = UUID.randomUUID();
        when(tablePort.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.getById(randomId))
                .isInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldReturnTableWhenGetByIdWithExistingId() {
        when(tablePort.findById(any(UUID.class))).thenReturn(Optional.of(Table.builder().build()));

        Table actual = tableService.getById(UUID.randomUUID());

        assertThat(actual).isNotNull();
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenAddWaiterWithNonExistingTableIdOrWaiterId() {
        UUID randomId = UUID.randomUUID();
        UUID existingId = UUID.randomUUID();
        when(tablePort.findById(any(UUID.class)))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(Table.builder().active(true).build()));
        when(userPort.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(user.getId()).thenReturn(UUID.randomUUID());

        assertThatThrownBy(() -> tableService.addWaiter(randomId, null, user))
                .isInstanceOf(NonExistingIdException.class);
        assertThatThrownBy(() -> tableService.addWaiter(existingId, new TableWaitersRequest(randomId), user))
                .isInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldThrowActiveStateExceptionWhenAddWaiterWithNonActiveTableOrWaiter() {
        UUID randomId = UUID.randomUUID();
        UUID existingId = UUID.randomUUID();
        when(tablePort.findById(any(UUID.class)))
                .thenReturn(Optional.of(Table.builder().active(false).build()))
                .thenReturn(Optional.of(Table.builder().active(true).build()));
        when(userPort.findById(any(UUID.class)))
                .thenReturn(Optional.of(User.builder().active(false).build()));
        when(user.getId()).thenReturn(UUID.randomUUID());

        assertThatThrownBy(() -> tableService.addWaiter(randomId, null, user))
                .isInstanceOf(ActiveStateException.class);
        assertThatThrownBy(() -> tableService.addWaiter(existingId, new TableWaitersRequest(randomId), user))
                .isInstanceOf(ActiveStateException.class);
    }

    @Test
    void shouldAddWaiterToTableWhenAddWaiterWithValidData() {
        UUID randomId = UUID.randomUUID();
        when(tablePort.findById(any(UUID.class)))
                .thenReturn(Optional.of(Table.builder().active(true).waitersId(Sets.newHashSet()).build()))
                .thenReturn(Optional.of(Table.builder().active(true).waitersId(Sets.newHashSet(randomId)).build()));
        when(userPort.findById(any(UUID.class)))
                .thenReturn(Optional.of(User.builder().id(randomId).active(true).build()))
                .thenReturn(Optional.of(User.builder().id(randomId).active(true).build()));
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);

        Table actualAddedWaiter = tableService.addWaiter(randomId, new TableWaitersRequest(randomId), user);
        Table actualHasWaiter = tableService.addWaiter(randomId, new TableWaitersRequest(randomId), user);

        verify(tablePort, times(1)).save(actualAddedWaiter);

        assertThat(actualAddedWaiter.getWaitersId()).contains(randomId);
        assertThat(actualHasWaiter.getWaitersId()).contains(randomId);
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenRemoveWaiterWithNonExistingTableIdOrWaiterId() {
        UUID randomId = UUID.randomUUID();
        UUID existingId = UUID.randomUUID();
        when(tablePort.findById(any(UUID.class)))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(Table.builder().active(true).build()));
        when(userPort.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(user.getId()).thenReturn(UUID.randomUUID());

        assertThatThrownBy(() -> tableService.removeWaiter(randomId, null, user))
                .isInstanceOf(NonExistingIdException.class);
        assertThatThrownBy(() -> tableService.removeWaiter(existingId, new TableWaitersRequest(randomId), user))
                .isInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldRemoveWaiterToTableWhenRemoveWaiterWithValidData() {
        UUID randomId = UUID.randomUUID();
        when(tablePort.findById(any(UUID.class)))
                .thenReturn(Optional.of(Table.builder().active(true).waitersId(Sets.newHashSet()).build()))
                .thenReturn(Optional.of(Table.builder().active(true).waitersId(Sets.newHashSet(randomId)).build()));
        when(userPort.findById(any(UUID.class)))
                .thenReturn(Optional.of(User.builder().id(randomId).active(true).build()))
                .thenReturn(Optional.of(User.builder().id(randomId).active(true).build()));
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);

        Table actualHasNotWaiter = tableService.removeWaiter(randomId, new TableWaitersRequest(randomId), user);
        Table actualRemovedWaiter = tableService.removeWaiter(randomId, new TableWaitersRequest(randomId), user);

        verify(tablePort, times(1)).save(actualRemovedWaiter);

        assertThat(actualHasNotWaiter.getWaitersId()).isEmpty();
        assertThat(actualRemovedWaiter.getWaitersId()).isEmpty();
    }

    @Test
    void shouldRemoveAllWaiterWhenRemoveAllWaiters() {
        when(tablePort.findById(any(UUID.class)))
                .thenReturn(Optional.of(Table.builder().active(true).waitersId(Sets.newHashSet(UUID.randomUUID(), UUID.randomUUID())).build()));
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);

        Table table = tableService.removeAllWaiters(UUID.randomUUID(), user);

        assertThat(table.getWaitersId()).isEmpty();

        verify(tablePort, times(1)).save(table);
    }

    @Test
    void shouldThrowUsernameNotFountExceptionWhenLoadByUsernameWithNonExistingUsername() {
        when(tablePort.findByName(any(String.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.loadUserByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void shouldReturnUserDetailsWhenLoadByUsernameWithExistingUsername() {
        String username = "username";
        when(tablePort.findByName(username)).thenReturn(Optional.of(Table.builder().name(username).build()));

        UserDetails actual = tableService.loadUserByUsername(username);

        assertThat(actual)
                .isNotNull()
                .isInstanceOf(Table.class)
                .extracting(UserDetails::getUsername).isEqualTo(username);
    }

    @Test
    void shouldReturnNullWhenGetOrderByTableIdWithTableWithoutOrderId() {
        when(tablePort.findById(any(UUID.class))).thenReturn(Optional.of(Table.builder().build()));

        Order order = tableService.getOrderByTableId(UUID.randomUUID());

        assertThat(order)
                .isNull();
    }

    @Test
    void shouldThrowNonExistingIdExceptionWhenGetOrderByTableIdWithTableWithNonExistingOrderId() {
        when(tablePort.findById(any(UUID.class))).thenReturn(Optional.of(Table.builder().orderId(UUID.randomUUID()).build()));
        when(orderPort.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.getOrderByTableId(UUID.randomUUID()))
                .isInstanceOf(NonExistingIdException.class);
    }

    @Test
    void shouldReturnOrderWhenGetOrderByTableIdWithValidData() {
        UUID orderId = UUID.randomUUID();
        when(tablePort.findById(any(UUID.class))).thenReturn(Optional.of(Table.builder().orderId(orderId).build()));
        when(orderPort.findById(orderId)).thenReturn(Optional.of(Order.builder().id(orderId).build()));

        Order order = tableService.getOrderByTableId(UUID.randomUUID());
        assertThat(order)
                .isNotNull()
                .extracting(Order::getId).isEqualTo(orderId);
    }

    @Test
    void shouldReturnWaitersWhenGetWaitersByTableId() {
        when(tablePort.findById(any(UUID.class))).thenReturn(Optional.of(Table.builder().waitersId(Sets.newHashSet(UUID.randomUUID(), UUID.randomUUID())).build()));
        when(userPort.findById(any(UUID.class))).thenReturn(Optional.of(User.builder().id(UUID.randomUUID()).build()));

        List<User> waiters = tableService.getWaitersByTableId(UUID.randomUUID());

        assertThat(waiters)
                .hasSize(2);

        verify(userPort, times(2)).findById(any(UUID.class));
    }

    @Test
    void shouldDeleteTableWhenDeleteTableById() {
        when(tablePort.findById(any(UUID.class))).thenReturn(Optional.of(Table.builder().build()));
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);

        tableService.delete(UUID.randomUUID(), user);

        verify(tablePort, times(1)).delete(any(Table.class));
    }

    @Test
    void shouldThrowValidationExceptionWhenCreateWithInvalidData() {
        when(tableValidator.validate(any(CreateTableRequest.class))).thenReturn(ValidationResult.invalid(Set.of()));
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);

        assertThatThrownBy(() -> tableService.create(new CreateTableRequest("name", 5, "type", UUID.randomUUID(), "password"), user))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldThrowActiveStateExceptionWhenCreateWithInactiveHall() {
        when(tableValidator.validate(any(CreateTableRequest.class))).thenReturn(ValidationResult.valid());
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(hallPort.findById(any(UUID.class))).thenReturn(Optional.of(Hall.builder().active(false).build()));

        assertThatThrownBy(() -> tableService.create(new CreateTableRequest("name", 5, "type", UUID.randomUUID(), "password"), user))
                .isInstanceOf(ActiveStateException.class);
    }

    @Test
    void shouldCreateTableWhenCreateWithValidData() {
        UUID hallId = UUID.randomUUID();
        CreateTableRequest createTableRequest = new CreateTableRequest("name", 5, "type", hallId, "password");
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(tableValidator.validate(any(CreateTableRequest.class))).thenReturn(ValidationResult.valid());
        when(hallPort.findById(any(UUID.class))).thenReturn(Optional.of(Hall.builder().id(hallId).active(true).build()));
        when(tablePort.findAll()).thenReturn(List.of());

        Table actual = tableService.create(createTableRequest, user);

        assertThat(actual)
                .isNotNull()
                .extracting(Table::getHallId).isEqualTo(hallId);

    }

    @Test
    void shouldThrowBadRequestWhenCreateWithNameAndPasswordThatAlreadyExist() {
        UUID hallId = UUID.randomUUID();
        CreateTableRequest createTableRequest = new CreateTableRequest("name", 5, "type", hallId, "password");
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(tableValidator.validate(any(CreateTableRequest.class))).thenReturn(ValidationResult.valid());
        when(hallPort.findById(any(UUID.class))).thenReturn(Optional.of(Hall.builder().id(hallId).active(true).build()));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(tablePort.findAll())
                .thenReturn(List.of(Table.builder().name("name").build()))
                .thenReturn(List.of(Table.builder().password("password").build()));

        assertThatThrownBy(() -> tableService.create(createTableRequest, user))
                .isExactlyInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> tableService.create(createTableRequest, user))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldThrowBadRequestWhenUpdateWithNameAndPasswordThatAlreadyExist() {
        UUID hallId = UUID.randomUUID();
        UpdateTableRequest updateTableRequest = new UpdateTableRequest("name", 5, "type", hallId, "password");
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(tableValidator.validate(any(UpdateTableRequest.class))).thenReturn(ValidationResult.valid());
        when(hallPort.findById(any(UUID.class))).thenReturn(Optional.of(Hall.builder().id(hallId).active(true).build()));
        when(tablePort.findById(any(UUID.class))).thenReturn(Optional.of(Table.builder().name("table").password("1111").build()));
        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false)
                .thenReturn(true);
        when(tablePort.findAll())
                .thenReturn(List.of(Table.builder().name("name").build()))
                .thenReturn(List.of(Table.builder().password("password").build()));

        assertThatThrownBy(() -> tableService.update(UUID.randomUUID(), updateTableRequest, user))
                .isExactlyInstanceOf(BadRequestException.class);
        assertThatThrownBy(() -> tableService.update(UUID.randomUUID(), updateTableRequest, user))
                .isExactlyInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldThrowValidationExceptionWhenUpdateWithInvalidData() {
        when(tableValidator.validate(any(UpdateTableRequest.class))).thenReturn(ValidationResult.invalid(Set.of()));
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(tablePort.findById(any(UUID.class))).thenReturn(Optional.of(Table.builder().name("table").password("1111").build()));

        assertThatThrownBy(() -> tableService.update(UUID.randomUUID(), new UpdateTableRequest("name", 5, "type", UUID.randomUUID(), "password"), user))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldUpdateTableWhenUpdateWithValidData() {
        UUID hallId = UUID.randomUUID();
        Table table = Table.builder().name("name").password("password").build();
        UpdateTableRequest updateTableRequest = new UpdateTableRequest("name", 5, TableType.VIP.name(), hallId, "password");
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(tableValidator.validate(any(UpdateTableRequest.class))).thenReturn(ValidationResult.valid());
        when(hallPort.findById(any(UUID.class))).thenReturn(Optional.of(Hall.builder().id(hallId).active(true).build()));
        when(tablePort.findAll()).thenReturn(List.of(table));
        when(tablePort.findById(any(UUID.class))).thenReturn(Optional.of(table));

        Table actual = tableService.update(UUID.randomUUID(), updateTableRequest, user);

        assertThat(actual)
                .isNotNull()
                .extracting(Table::getTableType).isEqualTo(TableType.VIP);
        assertThat(actual)
                .extracting(Table::getCapacity).isEqualTo(5);
    }

    @Test
    void shouldActivateTableWhenActivate() {
        Table table = Table.builder().name("name").active(false).password("password").build();
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(tablePort.findById(any(UUID.class)))
                .thenReturn(Optional.of(table))
                .thenReturn(Optional.of(Table.builder().active(true).build()));

        Table actualActivated = tableService.activate(UUID.randomUUID(), user);
        Table actualActive = tableService.activate(UUID.randomUUID(), user);

        assertThat(actualActivated)
                .isNotNull()
                .extracting(Table::isActive).isEqualTo(true);
        assertThat(actualActive)
                .isNotNull()
                .extracting(Table::isActive).isEqualTo(true);

        verify(tablePort, times(1)).save(actualActivated);
    }

    @Test
    void shouldDeactivateTableWhenDeactivate() {
        Table table = Table.builder().name("name").active(true).password("password").build();
        when(user.hasNoPermission(any(Permission.class))).thenReturn(false);
        when(tablePort.findById(any(UUID.class)))
                .thenReturn(Optional.of(table))
                .thenReturn(Optional.of(Table.builder().active(false).build()));

        Table actualDeactivated = tableService.deactivate(UUID.randomUUID(), user);
        Table actualInactive = tableService.deactivate(UUID.randomUUID(), user);

        assertThat(actualDeactivated)
                .isNotNull()
                .extracting(Table::isActive).isEqualTo(false);
        assertThat(actualInactive)
                .isNotNull()
                .extracting(Table::isActive).isEqualTo(false);

        verify(tablePort, times(1)).save(actualDeactivated);
    }
}
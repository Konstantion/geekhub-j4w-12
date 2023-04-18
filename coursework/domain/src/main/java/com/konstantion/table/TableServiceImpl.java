package com.konstantion.table;

import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.ValidationException;
import com.konstantion.exception.utils.ExceptionUtils;
import com.konstantion.hall.Hall;
import com.konstantion.hall.HallPort;
import com.konstantion.order.Order;
import com.konstantion.order.OrderPort;
import com.konstantion.order.OrderService;
import com.konstantion.table.model.CreateTableRequest;
import com.konstantion.table.model.TableWaitersRequest;
import com.konstantion.table.model.UpdateTableRequest;
import com.konstantion.table.validator.TableValidator;
import com.konstantion.user.User;
import com.konstantion.user.UserPort;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static com.konstantion.exception.utils.ExceptionMessages.NOT_ENOUGH_AUTHORITIES;
import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;
import static com.konstantion.user.Permission.*;
import static com.konstantion.user.Role.ADMIN;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Objects.requireNonNullElse;

@Component
public record TableServiceImpl(
        TableValidator tableValidator,
        TablePort tablePort,
        HallPort hallPort,
        OrderPort orderPort,
        UserPort userPort,
        OrderService orderService,
        PasswordEncoder passwordEncoder
) implements TableService {
    private static final Logger logger = LoggerFactory.getLogger(TableService.class);

    @Override
    public List<Table> getAll(boolean onlyActive) {
        List<Table> tables = tablePort.findAll();
        if (onlyActive) {
            return tables.stream().filter(Table::isActive).toList();
        }
        return tables;
    }

    @Override
    public Table addWaiter(UUID tableId, TableWaitersRequest request, User user) {
        if (user.hasNoPermission(ASSIGN_WAITER_TO_TABLE)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Table table = getByIdOrThrow(tableId);
        ExceptionUtils.isActiveOrThrow(table);

        User waiter = userPort.findById(request.waiterId())
                .orElseThrow(nonExistingIdSupplier(User.class, request.waiterId()));
        ExceptionUtils.isActiveOrThrow(waiter);

        if (table.getWaitersId().add(waiter.getId())) {
            tablePort.save(table);
            logger.info("Waiter with id {} successfully added to the table with id {}", waiter.getId(), tableId);
        } else {
            logger.warn("Table with id {} already has waiter with id {}", tableId, waiter.getId());
        }

        return table;
    }

    @Override
    public Table removeWaiter(UUID tableId, TableWaitersRequest request, User user) {
        if (user.hasNoPermission(ASSIGN_WAITER_TO_TABLE)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Table table = getByIdOrThrow(tableId);
        ExceptionUtils.isActiveOrThrow(table);

        User waiter = userPort.findById(request.waiterId())
                .orElseThrow(nonExistingIdSupplier(User.class, request.waiterId()));
        ExceptionUtils.isActiveOrThrow(waiter);

        if (table.getWaitersId().remove(waiter.getId())) {
            tablePort.save(table);
            logger.info("Waiter with id {} successfully removed from the table with id {}", waiter.getId(), tableId);
        } else {
            logger.warn("Table with id {} already hasn't waiter with id {}", tableId, waiter.getId());
        }

        return table;
    }

    @Override
    public Table update(UUID tableId, UpdateTableRequest request, User user) {
        Table table = getByIdOrThrow(tableId);

        tableValidator.validate(request).validOrTrow();

        List<Table> dbTables = tablePort.findAll();
        if (anyMatchTable(dbTables, Table::getName, request.name(), Objects::equals)) {
            throw new BadRequestException(format("Table with name %s already exist", request.name()));
        }

        if (anyMatchTable(dbTables, Table::getPassword, request.password(), passwordEncoder::matches)) {
            throw new BadRequestException(format("Table with password %s already exist", request.password()));
        }

        updateTable(table, request);

        tablePort.save(table);

        return table;
    }


    @Override
    public Table activate(UUID tableId, User user) {
        if (user.hasNoPermission(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to activate table");
        }

        Table table = getByIdOrThrow(tableId);

        if (table.isActive()) {
            return table;
        }

        prepareToActivate(table);
        tablePort.save(table);

        return table;
    }

    @Override
    public Table deactivate(UUID tableId, User user) {
        if (user.hasNoPermission(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to deactivate table");
        }

        Table table = getByIdOrThrow(tableId);

        canBeDeactivatedOrThrow(table);

        if (!table.isActive()) {
            return table;
        }

        prepareToDeactivate(table);
        tablePort.save(table);

        return table;
    }

    @Override
    public Table create(CreateTableRequest request, User user) {
        if (user.hasNoPermission(CREATE_TABLE)) {
            throw new ForbiddenException("Not enough authorities to create table");
        }

        ValidationResult validationResult =
                tableValidator.validate(request);

        if (validationResult.errorsPresent()) {
            throw new ValidationException("Validation error, table is invalid", validationResult.errorsMap());
        }

        Hall hall = hallPort.findById(request.hallUuid())
                .orElseThrow(nonExistingIdSupplier(Hall.class, request.hallUuid()));
        ExceptionUtils.isActiveOrThrow(hall);

        List<Table> dbTables = tablePort.findAll();
        if (anyMatchTable(dbTables, Table::getName, request.name(), Objects::equals)) {
            throw new BadRequestException(format("Table with name %s already exist", request.name()));
        }

        if (anyMatchTable(dbTables, Table::getPassword, request.password(), passwordEncoder::matches)) {
            throw new BadRequestException(format("Table with password %s already exist", request.password()));
        }


        Table table = buildTableFromCreateRequestAndHall(request, hall);

        tablePort.save(table);

        logger.info("Table {} successfully created", table);

        return table;
    }

    @Override
    public Table delete(UUID tableId, User user) {
        if (user.hasNoPermission(ADMIN, DELETE_TABLE)) {
            throw new ForbiddenException("Not enough authorities to delete table");
        }

        Table table = getByIdOrThrow(tableId);

        tablePort.delete(table);

        return table;
    }

    @Override
    public Table getById(UUID tableId) {
        return getByIdOrThrow(tableId);
    }

    @Override
    public Order getOrderByTableId(UUID tableId) {
        Table table = getByIdOrThrow(tableId);

        if (!table.hasOrder()) {
            return null;
        }

        Order order = orderPort.findById(table.getOrderId())
                .orElseThrow(nonExistingIdSupplier(Order.class, table.getOrderId()));

        logger.info("Returned order with id {} for table with id {}", order.getId(), tableId);

        return order;
    }

    @Override
    public List<User> getWaitersByTableId(UUID tableId) {
        Table table = getByIdOrThrow(tableId);

        return table.getWaitersId().stream()
                .map(userId ->
                        userPort.findById(userId)
                                .orElseThrow(nonExistingIdSupplier(User.class, userId)))
                .toList();
    }

    @Override
    public Table removeAllWaiters(UUID tableId, User user) {
        if (user.hasNoPermission(ASSIGN_WAITER_TO_TABLE)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Table table = getByIdOrThrow(tableId);
        table.getWaitersId().clear();

        tablePort.save(table);

        return table;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return tablePort.findByName(username).orElseThrow(() -> {
            throw new UsernameNotFoundException(format("User with username %s doesn't exist", username));
        });
    }

    private Table getByIdOrThrow(UUID tableId) {
        return tablePort.findById(tableId)
                .orElseThrow(nonExistingIdSupplier(Table.class, tableId));
    }

    private boolean canBeDeactivatedOrThrow(Table table) {
        if (table.hasOrder()) {
            throw new BadRequestException(format("Table with id %s has active order", table.getId()));
        }
        return true;
    }

    private void prepareToDeactivate(Table table) {
        table.setActive(false);
        table.setDeletedAt(now());
        table.getWaitersId().clear();
    }

    private void prepareToActivate(Table table) {
        table.setActive(true);
        table.setDeletedAt(null);
    }

    private <T> boolean anyMatchTable(List<Table> tables, Function<Table, T> extractor, T desired, BiPredicate<T, T> criteria) {
        return tables.stream()
                .map(extractor)
                .anyMatch(value -> criteria.test(desired, value));
    }

    private void updateTable(Table table, UpdateTableRequest updateTableRequest) {
        table.setName(requireNonNullElse(updateTableRequest.name(), table.getName()));
        table.setHallId(requireNonNullElse(updateTableRequest.hallUuid(), table.getHallId()));
        table.setCapacity(requireNonNullElse(updateTableRequest.capacity(), table.getCapacity()));
        table.setPassword(requireNonNullElse(passwordEncoder.encode(updateTableRequest.password()), table.getPassword()));
        table.setTableType(valueOfTableTypeOrDefault(updateTableRequest.tableType(), table.getTableType()));
    }

    private TableType valueOfTableTypeOrDefault(String tableType, TableType orDefault) {
        if (Arrays.stream(TableType.values()).map(TableType::name).anyMatch(type -> type.equals(tableType))) {
            return TableType.valueOf(tableType);
        }

        return orDefault;
    }

    private Table buildTableFromCreateRequestAndHall(CreateTableRequest request, Hall hall) {
        return Table.builder()
                .name(request.name())
                .capacity(request.capacity())
                .createdAt(now())
                .active(true)
                .waitersId(new HashSet<>())
                .hallId(hall.getId())
                .password(passwordEncoder.encode(request.password()))
                .tableType(valueOfTableTypeOrDefault(request.tableType(), TableType.COMMON))
                .build();
    }
}

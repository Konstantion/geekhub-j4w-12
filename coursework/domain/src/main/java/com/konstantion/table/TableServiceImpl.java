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
import com.konstantion.table.validator.TableValidator;
import com.konstantion.user.User;
import com.konstantion.user.UserService;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;
import static com.konstantion.user.Permission.CREATE_TABLE;
import static com.konstantion.user.Permission.DELETE_TABLE;
import static com.konstantion.user.Role.ADMIN;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Component
public record TableServiceImpl(
        TableValidator tableValidator,
        TablePort tablePort,
        HallPort hallPort,
        OrderPort orderPort,
        UserService userService,
        OrderService orderService,
        PasswordEncoder passwordEncoder
) implements TableService {
    private static final Logger logger = LoggerFactory.getLogger(TableService.class);

    @Override
    public List<Table> getAll() {
        return tablePort.findAll();
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
        if (anyMatchTableName(dbTables, request.name())) {
            throw new BadRequestException(format("Table with name %s already exist", request.name()));
        }

        if (anyMatchTablePassword(dbTables, request.password())) {
            throw new BadRequestException(format("Table with password %s already exist", request.password()));
        }


        Table table = Table.builder()
                .name(request.name())
                .capacity(request.capacity())
                .createdAt(now())
                .active(true)
                .waitersId(new ArrayList<>())
                .hallId(hall.getId())
                .password(passwordEncoder.encode(request.password()))
                .tableType(TableType.valueOf(request.tableType()))
                .build();

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

    private boolean anyMatchTableName(List<Table> tables, String name) {
        return tables.stream().anyMatch(table -> table.getName().equals(name));
    }

    private boolean anyMatchTablePassword(List<Table> tables, String password) {
        return tables.stream()
                .anyMatch(table -> passwordEncoder.matches(password, table.getPassword()));
    }
}

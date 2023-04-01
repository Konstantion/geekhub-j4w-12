package com.konstantion.table;

import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.ValidationException;
import com.konstantion.hall.HallService;
import com.konstantion.hall.dto.HallDto;
import com.konstantion.order.OrderService;
import com.konstantion.order.dto.OrderDto;
import com.konstantion.table.dto.CreationTableDto;
import com.konstantion.table.dto.TableDto;
import com.konstantion.table.validator.TableValidator;
import com.konstantion.user.User;
import com.konstantion.user.UserService;
import com.konstantion.exception.utils.ExceptionUtils;
import com.konstantion.utils.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

import static com.konstantion.user.Permission.*;
import static com.konstantion.user.Role.ADMIN;
import static com.konstantion.exception.utils.ExceptionMessages.NOT_ENOUGH_AUTHORITIES;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Component
public record TableServiceImpl(
        TableValidator tableValidator,
        TableRepository tableRepository,
        HallService hallService,
        UserService userService,
        OrderService orderService
) implements TableService {
    private static final Logger logger = LoggerFactory.getLogger(TableService.class);
    private static final TableMapper tableMapper = TableMapper.INSTANCE;

    @Override
    public TableDto activate(UUID tableId, User user) {
        if (user.hasNoPermission(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to activate table");
        }

        Table table = getByIdOrThrow(tableId);

        if (table.isActive()) {
            return tableMapper.toDto(table);
        }

        prepareToActivate(table);
        tableRepository.save(table);

        return tableMapper.toDto(table);
    }

    @Override
    public TableDto deactivate(UUID tableId, User user) {
        if (user.hasNoPermission(ADMIN)) {
            throw new ForbiddenException("Not enough authorities to deactivate table");
        }

        Table table = getByIdOrThrow(tableId);

        canBeDeactivatedOrThrow(table);

        if (!table.isActive()) {
            return tableMapper.toDto(table);
        }

        prepareToDeactivate(table);
        tableRepository.save(table);

        return tableMapper.toDto(table);
    }

    @Override
    public TableDto create(CreationTableDto ctdto, User user) {
        if (user.hasNoPermission(CREATE_TABLE)) {
            throw new ForbiddenException("Not enough authorities to create table");
        }

        ValidationResult validationResult =
                tableValidator.validate(ctdto);

        if (validationResult.errorsPresent()) {
            throw new ValidationException("Validation error, table is invalid", validationResult.errorsMap());
        }

        HallDto hallDto = hallService.findHallById(ctdto.hallUuid());

        Table table = Table.builder()
                .name(ctdto.name())
                .capacity(ctdto.capacity())
                .createdAt(now())
                .active(true)
                .waitersId(new ArrayList<>())
                .hallId(hallDto.id())
                .tableType(TableType.valueOf(ctdto.tableType()))
                .build();

        tableRepository.save(table);

        TableDto tableDto = tableMapper.toDto(table);
        logger.info("Table {} successfully created", tableDto);

        return tableDto;
    }

    @Override
    public TableDto delete(UUID tableId, User user) {
        if (user.hasNoPermission(ADMIN, DELETE_TABLE)) {
            throw new ForbiddenException("Not enough authorities to delete table");
        }

        Table table = getByIdOrThrow(tableId);

        tableRepository.delete(table);

        return tableMapper.toDto(table);
    }

    @Override
    public TableDto getById(UUID tableId, User user) {
        return tableMapper.toDto(getByIdOrThrow(tableId));
    }

    @Override
    public TableDto clearOrder(UUID tableId, User user) {
        Table table = getByIdOrThrow(tableId);

        if (!table.hasOrder()) {
            throw new BadRequestException(format("Table with id %s doesn't have active order", tableId));
        }

        table.setOrderId(null);
        tableRepository.save(table);

        return tableMapper.toDto(table);
    }

    @Override
    public TableDto setOrder(UUID tableId, UUID orderId, User user) {
        if(user.hasNoPermission(TRANSFER_ORDER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Table table = getByIdOrThrow(tableId);
        ExceptionUtils.isActiveOrThrow(table);

        OrderDto order = orderService.getById(orderId);
        ExceptionUtils.isActiveOrThrow(order);

        table.setOrderId(orderId);
        tableRepository.save(table);

        return tableMapper.toDto(table);
    }

    private Table getByIdOrThrow(UUID tableId) {
        return tableRepository.findById(tableId).orElseThrow(() ->
                new BadRequestException(format("Table with id %s doesn't exist", tableId))
        );
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

}

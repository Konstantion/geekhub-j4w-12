package com.konstantion.table;

import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.ValidationException;
import com.konstantion.hall.HallService;
import com.konstantion.hall.dto.HallDto;
import com.konstantion.table.dto.CreationTableDto;
import com.konstantion.table.dto.TableDto;
import com.konstantion.table.validator.TableValidator;
import com.konstantion.user.Permission;
import com.konstantion.user.Role;
import com.konstantion.user.User;
import com.konstantion.user.UserService;
import com.konstantion.utils.validator.ValidationResult;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

import static com.konstantion.user.Permission.CREATE_TABLE;
import static com.konstantion.user.Permission.DELETE_TABLE;
import static com.konstantion.user.Role.ADMIN;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Component
public record TableServiceImp(
        TableValidator tableValidator,
        TableRepository tableRepository,
        HallService hallService,
        UserService userService
) implements TableService {
    private static final Logger logger = LoggerFactory.getLogger(TableService.class);
    private static final TableMapper tableMapper = TableMapper.INSTANCE;

    @Override
    public TableDto createTable(CreationTableDto cdto, User user) {
        if (user.hasNoPermission(ADMIN, CREATE_TABLE)) {
            throw new ForbiddenException("You don't have enough authorities to create table");
        }

        ValidationResult validationResult =
                tableValidator.validate(cdto);

        if (validationResult.errorsPresent()) {
            throw new ValidationException("Validation error, table is invalid", validationResult.errorsMap());
        }

        HallDto hallDto = hallService.findHallById(cdto.hallUuid());

        User tableUser = createTableUser(cdto, hallDto);
        userService.saveTableUser(user);

        Table table = Table.builder()
                .name(cdto.name())
                .capacity(cdto.capacity())
                .createdAt(now())
                .active(true)
                .waiters(new ArrayList<>())
                .tableUser(tableUser)
                .tableType(TableType.valueOf(cdto.tableType()))
                .build();

        tableRepository.saveAndFlush(table);

        TableDto tableDto = tableMapper.toDto(table);
        logger.info("Table {} successfully created", tableDto);

        return tableDto;
    }

    @Override
    public TableDto deleteTable(UUID tableUuid, User user) {
        if (user.hasNoPermission(ADMIN, DELETE_TABLE)) {
            throw new ForbiddenException("You don't have enough authorities to delete table");
        }

        Table table = getByIdOrThrow(tableUuid);

        canBeDeletedOrThrow(table);

        prepareToDelete(table);

        tableRepository.delete(table);

        return tableMapper.toDto(table);
    }

    private Table getByIdOrThrow(UUID tableUuid) {
        return tableRepository.findById(tableUuid).orElseThrow(() ->
                new BadRequestException(format("Table with id %s doesn't exist", tableUuid))
        );
    }

    private boolean canBeDeletedOrThrow(Table table) {
        if (table.hasOrder()) {
            throw new BadRequestException(format("Table with id %s has active order", table.getId()));
        } else if (!table.isActive()) {
            throw new BadRequestException(format("Table with id %s isn't active", table.getId()));
        }

        return true;
    }

    private void prepareToDelete(Table table) {
        table.setName(table.getName() + "_DELETED");
        table.setActive(false);
        table.setDeletedAt(now());
        table.getWaiters().clear();
    }

    private User createTableUser(CreationTableDto cdto, HallDto hallDto) {
        return User.builder()
                .username(getTableUsername(cdto, hallDto))
                .roles(Role.getTableRole())
                .permissions(Permission.getDefaultTablePermissions())
                .active(true)
                .createdAt(now())
                .password(cdto.password())
                .build();
    }

    private String getTableUsername(CreationTableDto cdto, HallDto hallDto) {
        String username = cdto.username();

        if (isBlank(username)) {
            username = cdto.name() + hallDto.name() + "_TABLE";
        }

        return username;
    }
}

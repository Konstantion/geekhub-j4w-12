package com.konstantion.hall;

import com.konstantion.exception.ForbiddenException;
import com.konstantion.hall.model.CreateHallRequest;
import com.konstantion.hall.model.UpdateHallRequest;
import com.konstantion.hall.validator.HallValidator;
import com.konstantion.table.Table;
import com.konstantion.table.TablePort;
import com.konstantion.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionMessages.NOT_ENOUGH_AUTHORITIES;
import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;
import static com.konstantion.user.Permission.*;
import static com.konstantion.utils.ObjectUtils.requireNonNullOrElseNullable;
import static java.time.LocalDateTime.now;

@Component
public record HallServiceImpl(
        HallPort hallPort,
        TablePort tablePort,
        HallValidator hallValidator
) implements HallService {
    private static final Logger logger = LoggerFactory.getLogger(HallServiceImpl.class);

    @Override
    public Hall create(CreateHallRequest request, User user) {
        if (user.hasNoPermission(CREATE_HALL)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        hallValidator.validate(request).validOrTrow();

        Hall hall = Hall.builder()
                .name(request.name())
                .active(true)
                .createdAt(now())
                .build();

        hallPort.save(hall);
        logger.info("Hall with id {} successfully created and returned", hall.getId());
        return hall;
    }

    @Override
    public Hall getById(UUID id) {
        Hall hall = getByIdOrThrow(id);
        logger.info("Hall with id {} successfully returned", hall.getId());
        return hall;
    }

    @Override
    public List<Hall> getAll(boolean onlyActive) {
        List<Hall> halls = hallPort.findAll();
        if (onlyActive) {
            return halls.stream().filter(Hall::isActive).toList();
        }
        logger.info("All halls successfully returned");
        return halls;
    }

    @Override
    public Hall update(UUID id, UpdateHallRequest request, User user) {
        if (user.hasNoPermission(UPDATE_HALL)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        hallValidator.validate(request).validOrTrow();

        Hall hall = getByIdOrThrow(id);

        updateHall(hall, request);

        hallPort.save(hall);
        logger.info("Hall with id {} successfully updated and returned", id);
        return hall;
    }

    @Override
    public Hall activate(UUID id, User user) {
        if (user.hasNoPermission(CHANGE_HALL_STATE)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Hall hall = getByIdOrThrow(id);

        if (hall.isActive()) {
            logger.warn("Hall with id {} is already active and returned", id);
            return hall;
        }

        prepareToActivate(hall);

        hallPort.save(hall);
        logger.info("Hall with id {} successfully activated and returned", id);
        return hall;
    }

    @Override
    public Hall deactivate(UUID id, User user) {
        if (user.hasNoPermission(CHANGE_HALL_STATE)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Hall hall = getByIdOrThrow(id);

        if (!hall.isActive()) {
            logger.warn("Hall with id {} is already inactive and returned", id);
            return hall;
        }

        prepareToDeactivate(hall);

        hallPort.save(hall);
        logger.info("Hall with id {} successfully deactivated and returned", id);
        return hall;
    }

    @Override
    public Hall delete(UUID id, User user) {
        if (user.hasNoPermission(DELETE_HALL)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }
        Hall hall = getByIdOrThrow(id);

        hallPort.delete(hall);
        logger.info("Hall with id {} successfully deleted", id);
        return hall;
    }

    @Override
    public List<Table> getTablesByHallId(UUID id, boolean onlyActive) {
        getByIdOrThrow(id);

        List<Table> tables = tablePort.findAllWhereHallId(id);
        if(onlyActive) {
            tables = tables.stream().filter(Table::isActive).toList();
        }
        logger.info("Tables in hall with id {} successfully returned", id);
        return tables;
    }

    private Hall getByIdOrThrow(UUID id) {
        return hallPort.findById(id)
                .orElseThrow(nonExistingIdSupplier(Hall.class, id));
    }

    private void prepareToActivate(Hall hall) {
        hall.setActive(true);
    }

    private void prepareToDeactivate(Hall hall) {
        hall.setActive(false);
    }

    private void updateHall(Hall hall, UpdateHallRequest request) {
        hall.setName(requireNonNullOrElseNullable(request.name(), hall.getName()));
    }
}

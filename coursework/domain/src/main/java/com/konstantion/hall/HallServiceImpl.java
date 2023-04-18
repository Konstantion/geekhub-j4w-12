package com.konstantion.hall;

import com.konstantion.exception.ForbiddenException;
import com.konstantion.hall.model.CreateHallRequest;
import com.konstantion.table.Table;
import com.konstantion.table.TablePort;
import com.konstantion.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionMessages.NOT_ENOUGH_AUTHORITIES;
import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;
import static com.konstantion.user.Permission.CREATE_TABLE;
import static com.konstantion.user.Role.ADMIN;
import static java.time.LocalDateTime.now;

@Component
public record HallServiceImpl(
        HallPort hallPort,
        TablePort tablePort
) implements HallService {

    @Override
    public Hall create(CreateHallRequest request, User user) {
        if (user.hasNoPermission(CREATE_TABLE)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Hall hall = Hall.builder()
                .name(request.name())
                .active(true)
                .createdAt(now())
                .build();

        hallPort.save(hall);

        return hall;
    }

    @Override
    public Hall getById(UUID id) {
        return findByIdOrThrow(id);
    }

    @Override
    public List<Hall> getAll(boolean onlyActive) {
        List<Hall> halls = hallPort.findAll();
        if (onlyActive) {
            return halls.stream().filter(Hall::isActive).toList();
        }
        return halls;
    }

    @Override
    public Hall activate(UUID id, User user) {
        if (user.hasNoPermission(ADMIN)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Hall hall = findByIdOrThrow(id);

        if (hall.isActive()) {
            return hall;
        }

        prepareToActivate(hall);

        hallPort.save(hall);

        return hall;
    }

    @Override
    public Hall deactivate(UUID id, User user) {
        if (user.hasNoPermission(ADMIN)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Hall hall = findByIdOrThrow(id);

        if (!hall.isActive()) {
            return hall;
        }

        prepareToDeactivate(hall);

        hallPort.save(hall);

        return hall;
    }

    @Override
    public Hall delete(UUID id, User user) {
        Hall hall = findByIdOrThrow(id);

        hallPort.delete(hall);
        return hall;
    }

    @Override
    public List<Table> getTablesByHallId(UUID id) {
        findByIdOrThrow(id);

        return tablePort.findAllWhereHallId(id);
    }

    private Hall findByIdOrThrow(UUID id) {
        return hallPort.findById(id)
                .orElseThrow(nonExistingIdSupplier(Hall.class, id));
    }

    private void prepareToActivate(Hall hall) {
        hall.setActive(true);
    }

    private void prepareToDeactivate(Hall hall) {
        hall.setActive(false);
    }
}

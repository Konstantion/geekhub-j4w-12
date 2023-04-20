package com.konstantion.call;

import com.konstantion.call.model.CreateCallRequest;
import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
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
import static com.konstantion.user.Permission.CLOSE_CALL;
import static com.konstantion.user.Permission.SUPER_USER;
import static com.konstantion.user.Role.ADMIN;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Component
public record CallServiceImpl(
        CallPort callPort,
        TablePort tablePort
) implements CallService {
    private static final Logger logger = LoggerFactory.getLogger(CallServiceImpl.class);

    @Override
    public List<Call> getAll() {
        return callPort.findAll();
    }

    @Override
    public List<Call> getAllByUser(User user) {
        return callPort.findAll()
                .stream()
                .filter(call -> call.getWaitersId().contains(user.getId()))
                .toList();
    }

    @Override
    public Call createCall(CreateCallRequest request) {
        UUID tableId = request.tableId();
        Purpose purpose = request.purpose();

        Table table = tablePort.findById(tableId)
                .orElseThrow(nonExistingIdSupplier(Table.class, tableId));

        callPort.findByTableIdAndPurpose(tableId, purpose).ifPresent(call -> {
            throw new BadRequestException(format("Call with purpose %s for table %s already exist", purpose, tableId));
        });

        Call call = Call.builder()
                .purpose(purpose)
                .tableId(tableId)
                .waitersId(table.getWaitersId())
                .openedAt(now())
                .build();

        callPort.save(call);

        return call;
    }

    @Override
    public Call closeCall(UUID callId, User user) {
        if (user.hasNoPermission(CLOSE_CALL)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }
        Call call = getByIdOrThrow(callId);

        if (!call.getWaitersId().contains(user.getId())
            && !user.hasNoPermission(ADMIN)) {
            throw new BadRequestException(format("Call with id %s doesn't related with user with id %s", callId, user.getId()));
        }

        callPort.delete(call);

        return call;
    }

    private Call getByIdOrThrow(UUID callId) {
        return callPort.findById(callId)
                .orElseThrow(nonExistingIdSupplier(Call.class, callId));
    }
}

package com.konstantion.hall;

import com.konstantion.hall.model.CreateHallRequest;
import com.konstantion.user.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionUtils.nonExistingIdSupplier;

@Component
public record HallServiceImpl(
        HallPort hallPort
) implements HallService {

    @Override
    public Hall create(CreateHallRequest createHallRequest, User user) {
        return null;
    }

    @Override
    public Hall findHallById(UUID id, User user) {
        return findByIdOrThrow(id);
    }

    private Hall findByIdOrThrow(UUID id) {
        return hallPort.findById(id)
                .orElseThrow(nonExistingIdSupplier(Hall.class, id));
    }
}

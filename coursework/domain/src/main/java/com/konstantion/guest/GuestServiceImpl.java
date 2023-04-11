package com.konstantion.guest;

import com.konstantion.exception.BadRequestException;
import com.konstantion.guest.model.CreateGuestRequest;
import com.konstantion.guest.model.EditGuestRequest;
import com.konstantion.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
public record GuestServiceImpl(
        GuestPort guestPort
) implements GuestService {
    private static final Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);

    @Override
    public List<Guest> getAll() {
        return null;
    }

    @Override
    public Guest getById(UUID id) {
        return getByIdOrThrow(id);
    }

    @Override
    public Guest create(CreateGuestRequest cgdto, User user) {
        return null;
    }

    @Override
    public Guest edit(UUID id, EditGuestRequest cgdto, User user) {
        return null;
    }

    @Override
    public Guest delete(UUID id, User user) {
        return null;
    }

    private Guest getByIdOrThrow(UUID id) {
        return guestPort.findById(id).orElseThrow(() -> {
            throw new BadRequestException(format("Guest with id %s, doesn't exist", id));
        });
    }
}

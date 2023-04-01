package com.konstantion.guest;

import com.konstantion.exception.BadRequestException;
import com.konstantion.guest.dto.CreationGuestDto;
import com.konstantion.guest.dto.GuestDto;
import com.konstantion.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.lang.String.format;

@Service
public record GuestServiceImpl(
        GuestRepository guestRepository
) implements GuestService {
    private static final Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);
    private static final GuestMapper guestMapper = GuestMapper.INSTANCE;

    @Override
    public GuestDto getById(UUID id) {
        return guestMapper.toDto(getByIdOrThrow(id));
    }

    @Override
    public GuestDto create(CreationGuestDto cgdto, User user) {
        return null;
    }

    @Override
    public GuestDto edit(UUID id, CreationGuestDto cgdto, User user) {
        return null;
    }

    @Override
    public GuestDto delete(UUID id, User user) {
        return null;
    }

    private Guest getByIdOrThrow(UUID id) {
        return guestRepository.findById(id).orElseThrow(() -> {
            throw new BadRequestException(format("Guest with id %s, doesn't exist", id));
        });
    }
}

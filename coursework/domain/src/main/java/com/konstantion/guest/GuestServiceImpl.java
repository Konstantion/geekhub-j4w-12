package com.konstantion.guest;

import com.konstantion.exception.BadRequestException;
import com.konstantion.exception.ForbiddenException;
import com.konstantion.exception.utils.ExceptionUtils;
import com.konstantion.guest.model.CreateGuestRequest;
import com.konstantion.guest.model.UpdateGuestRequest;
import com.konstantion.guest.validator.GuestValidator;
import com.konstantion.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.konstantion.exception.utils.ExceptionMessages.NOT_ENOUGH_AUTHORITIES;
import static com.konstantion.user.Permission.*;
import static com.konstantion.utils.ObjectUtils.requireNonNullOrElseNullable;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Service
public record GuestServiceImpl(
        GuestPort guestPort,
        GuestValidator guestValidator
) implements GuestService {
    private static final Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);

    @Override
    public List<Guest> getAll(boolean onlyActive) {
        List<Guest> guests = guestPort.findAll();
        if (onlyActive) {
            return guests.stream().filter(Guest::isActive).toList();
        }
        return guests;
    }

    @Override
    public Guest getById(UUID id) {
        return getByIdOrThrow(id);
    }

    @Override
    public Guest create(CreateGuestRequest request, User user) {
        if (user.hasNoPermission(CREATE_GUEST)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        guestValidator.validate(request).validOrTrow();

        guestPort.findByName(request.name()).orElseThrow(() -> {
            throw new BadRequestException(format("Guest with name %s already exist", request.name()));
        });

        Guest guest = Guest.builder()
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .discountPercent(request.discountPercent())
                .createdAt(now())
                .totalSpentSum(0.0)
                .build();

        guestPort.save(guest);

        return guest;
    }

    @Override
    public Guest update(UUID id, UpdateGuestRequest request, User user) {
        if (user.hasNoPermission(CREATE_GUEST)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        guestValidator.validate(request).validOrTrow();

        Guest guest = getByIdOrThrow(id);
        ExceptionUtils.isActiveOrThrow(guest);

        if (!guest.getName().equals(request.name())) {
            guestPort.findByName(request.name()).orElseThrow(() -> {
                throw new BadRequestException(format("Guest with name %s already exist", request.name()));
            });
        }

        updateGuest(guest, request);

        guestPort.save(guest);

        return guest;
    }

    private void updateGuest(Guest guest, UpdateGuestRequest request) {
        guest.setName(requireNonNullOrElseNullable(request.name(), guest.getName()));
        guest.setPhoneNumber(requireNonNullOrElseNullable(request.phoneNumber(), guest.getPhoneNumber()));
        guest.setDiscountPercent(requireNonNullOrElseNullable(request.discountPercent(), guest.getDiscountPercent()));
    }

    @Override
    public Guest activate(UUID id) {
        Guest guest = getByIdOrThrow(id);

        if (guest.isActive()) {
            return guest;
        }

        prepareToActivate(guest);

        guestPort.save(guest);

        return guest;
    }

    @Override
    public Guest deactivate(UUID id) {
        Guest guest = getByIdOrThrow(id);

        if (!guest.isActive()) {
            return guest;
        }

        prepareToDeactivate(guest);

        guestPort.save(guest);

        return guest;
    }

    @Override
    public Guest delete(UUID id, User user) {
        if (user.hasNoPermission(DELETE_GUEST)
            && user.hasNoPermission(SUPER_USER)) {
            throw new ForbiddenException(NOT_ENOUGH_AUTHORITIES);
        }

        Guest guest = getByIdOrThrow(id);

        guestPort.delete(guest);

        return guest;
    }

    private Guest getByIdOrThrow(UUID id) {
        return guestPort.findById(id).orElseThrow(() -> {
            throw new BadRequestException(format("Guest with id %s, doesn't exist", id));
        });
    }

    private void prepareToDeactivate(Guest guest) {
        guest.setActive(false);
    }

    private void prepareToActivate(Guest guest) {
        guest.setActive(true);
    }
}

package com.konstantion.hall;

import com.konstantion.exception.BadRequestException;
import com.konstantion.hall.dto.HallDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.lang.String.format;

@Component
public record HallServiceImp(
        HallRepository hallRepository
) implements HallService {
    private static final HallMapper hallMapper = HallMapper.INSTANCE;
    @Override
    public HallDto findHallById(UUID uuid) {
        Hall hall = hallRepository.findById(uuid).orElseThrow(() ->
                new BadRequestException(format("Hall with id %s doesn't exist", uuid))
        );

        return hallMapper.toDto(hall);
    }
}

package com.konstantion.hall;

import com.konstantion.hall.dto.HallDto;

import java.util.UUID;

public interface HallService {
    HallDto findHallById(UUID uuid);
}

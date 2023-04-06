package com.konstantion.hall;

import com.konstantion.hall.model.CreateHallRequest;
import com.konstantion.user.User;

import java.util.UUID;

public interface HallService {
    Hall create(CreateHallRequest createHallRequest, User user);
    Hall findHallById(UUID uuid, User user);
}

package com.konstantion.hall;

import java.util.Optional;
import java.util.UUID;

public interface HallPort {
    Optional<Hall> findById(UUID uuid);

    Hall save(Hall hall);

    void delete(Hall hall);
}

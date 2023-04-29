package com.konstantion.hall;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HallPort {
    List<Hall> findAll();
    Optional<Hall> findById(UUID uuid);

    Hall save(Hall hall);

    void delete(Hall hall);
    void deleteAll();
}

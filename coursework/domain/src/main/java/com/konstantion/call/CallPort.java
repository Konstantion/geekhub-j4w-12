package com.konstantion.call;

import java.util.Optional;
import java.util.UUID;

public interface CallPort {
    Optional<Call> findById(UUID id);

    Call save(Call call);

    void delete(Call call);
}

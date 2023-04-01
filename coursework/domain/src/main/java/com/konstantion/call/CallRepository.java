package com.konstantion.call;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CallRepository {
    Call findById(UUID id);

    Call save(Call call);

    void delete(Call call);
}

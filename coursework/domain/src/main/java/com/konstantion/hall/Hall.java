package com.konstantion.hall;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Hall {
    private UUID id;
    private String name;
    private LocalDateTime createdAt;
    private Boolean active;

    public Hall() {
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hall hall)) return false;
        return Objects.equal(id, hall.id) && Objects.equal(name, hall.name) && Objects.equal(createdAt, hall.createdAt) && Objects.equal(active, hall.active);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, createdAt, active);
    }
}

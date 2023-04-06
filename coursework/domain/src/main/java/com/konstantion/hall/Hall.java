package com.konstantion.hall;

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
}

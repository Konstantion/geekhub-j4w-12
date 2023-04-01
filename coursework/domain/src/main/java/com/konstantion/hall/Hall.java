package com.konstantion.hall;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

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
}

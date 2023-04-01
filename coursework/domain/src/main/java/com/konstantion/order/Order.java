package com.konstantion.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Builder
@AllArgsConstructor
@Getter
@Setter
public class Order {
    private UUID id;
    private List<UUID> productsId;
    private UUID tableId;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private Boolean active;

    public Order() {

    }

    public boolean isActive() {
        return active;
    }
}

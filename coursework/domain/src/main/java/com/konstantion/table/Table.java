package com.konstantion.table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Table {
    private UUID id;
    private String name;
    private Integer capacity;
    private TableType tableType;
    private UUID hallId;
    private UUID orderId;
    @Builder.Default
    private List<UUID> waitersId = new ArrayList<>();
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public Table() {
    }

    public boolean hasOrder() {
        return nonNull(orderId);
    }

    public void removeOrder() {
        this.orderId = null;
    }

    public boolean hasWaiters() {
        return waitersId.isEmpty();
    }

    public boolean isActive() {
        return active;
    }
}

package com.konstantion.order;

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
public class Order {
    private UUID id;
    @Builder.Default
    private List<UUID> productsId = new ArrayList<>();
    private UUID tableId;
    private UUID userId;
    private UUID billId;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    @Builder.Default
    private Boolean active = true;

    public Order() {
    }

    public boolean isActive() {
        return active;
    }

    public boolean hasBill() {
        return nonNull(billId);
    }

    public void removeBill() {
        this.billId = null;
    }
}

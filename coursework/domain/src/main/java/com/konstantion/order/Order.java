package com.konstantion.order;

import com.google.common.base.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equal(id, order.id) && Objects.equal(productsId, order.productsId) && Objects.equal(tableId, order.tableId) && Objects.equal(userId, order.userId) && Objects.equal(billId, order.billId) && Objects.equal(createdAt, order.createdAt) && Objects.equal(closedAt, order.closedAt) && Objects.equal(active, order.active);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, productsId, tableId, userId, billId, createdAt, closedAt, active);
    }
}

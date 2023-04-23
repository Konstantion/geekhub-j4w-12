package com.konstantion.bill;

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
public class Bill {
    private UUID id;
    private UUID waiterId;
    private UUID orderId;
    private UUID guestId;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private Boolean active;
    private Double price;
    private Double priceWithDiscount;

    public boolean isActive() {
        return active;
    }
    public Bill() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bill bill)) return false;
        return Objects.equal(id, bill.id) && Objects.equal(waiterId, bill.waiterId) && Objects.equal(orderId, bill.orderId) && Objects.equal(guestId, bill.guestId) && Objects.equal(createdAt, bill.createdAt) && Objects.equal(closedAt, bill.closedAt) && Objects.equal(active, bill.active) && Objects.equal(price, bill.price) && Objects.equal(priceWithDiscount, bill.priceWithDiscount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, waiterId, orderId, guestId, createdAt, closedAt, active, price, priceWithDiscount);
    }
}

package com.konstantion.bill;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
}

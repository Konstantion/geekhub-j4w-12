package com.konstantion.bill;

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
public class Bill {
    private UUID id;
    private List<UUID> productsId;
    private UUID waiterId;
    private UUID orderId;
    private UUID guestId;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private Boolean active;
    private Double price;
    private Double priceWithDiscount;
}

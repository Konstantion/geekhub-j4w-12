package com.konstantion.guest;

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
public class Guest {
    private UUID id;
    private String name;
    private String phoneNumber;
    private Double discountPercent;
    private LocalDateTime createdAt;
    private Double totalSpentSum;
    private Boolean active;

    public Guest() {
    }

    public boolean isActive() {
        return active;
    }
    public void addToTotalSum(Double value) {
        totalSpentSum += value;
    }
}

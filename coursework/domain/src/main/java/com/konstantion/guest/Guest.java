package com.konstantion.guest;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Guest guest)) return false;
        return Objects.equal(id, guest.id) && Objects.equal(name, guest.name) && Objects.equal(phoneNumber, guest.phoneNumber) && Objects.equal(discountPercent, guest.discountPercent) && Objects.equal(createdAt, guest.createdAt) && Objects.equal(totalSpentSum, guest.totalSpentSum) && Objects.equal(active, guest.active);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, phoneNumber, discountPercent, createdAt, totalSpentSum, active);
    }
}

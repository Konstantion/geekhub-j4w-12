package com.konstantion.bill.model;

import java.util.UUID;

public record CreateBillRequest(
        UUID orderId,
        UUID guestId
) {
}

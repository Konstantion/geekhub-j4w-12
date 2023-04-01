package com.konstantion.bill.dto;

import java.util.UUID;

public record CreationBillDto(
        UUID orderId ,
        UUID guestId
) {
}

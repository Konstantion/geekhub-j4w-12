package com.konstantion.table.model;

import java.util.UUID;

public record TableWaitersRequest(
        UUID waiterId
) {
}

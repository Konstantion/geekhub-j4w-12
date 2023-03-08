package com.konstantion.order;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {
    @Test
    void process_shouldCreateNewOrderWithId_whenSetId() {
        Order order = Order.builder().build();
        UUID uuid = UUID.randomUUID();
        order = order.setUuid(uuid);

        assertThat(order.uuid()).isEqualTo(uuid);
    }
}
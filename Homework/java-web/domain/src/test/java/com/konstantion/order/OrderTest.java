package com.konstantion.order;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {
    @Test
    void process_shouldCreateNewOrderWithId_whenSetId() {
        Order order = Order.builder().build();

        order = order.setId(1L);

        assertThat(order.id()).isEqualTo(1L);
    }
}
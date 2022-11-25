package edu.geekhub.orcostat.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class DroneTest {
    @Test
    void default_drone_price_is_lada_vesta_sport() {
        final Drone drone = new Drone();

        assertEquals(200_000, drone.getPrice());
    }

    @Test
    void major_drone_price_can_differ_to_lada_vesta_sport() {
        final Drone drone = new Drone(30_000);

        assertEquals(30_000, drone.getPrice());
    }

    @Test
    void cannot_create_drone_with_negative_price() {
        Exception thrown = assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new Drone(-100)
        );

        assertEquals(
                "Price cannot be negative number",
                thrown.getMessage());
    }

    @Test
    void new_drone_has_date() {
        final Drone drone = new Drone();

        assertEquals(LocalDate.now(), drone.getDate());
    }

    @Test
    void new_drone_with_defined_date() {
        final Drone drone = new Drone(LocalDate.parse("2022-11-24"));

        LocalDate actualDate = LocalDate.of(2022, 11, 24);

        assertEquals(actualDate, drone.getDate());
    }

    @Test
    void new_drone_with_date_and_price() {
        final Drone drone = new Drone(1_000, LocalDate.parse("2022-11-24"));

        LocalDate actualDate = LocalDate.of(2022, 11, 24);
        int actualPrice = 1000;

        assertEquals(actualDate, drone.getDate());
        assertEquals(actualPrice, drone.getPrice());
    }
}
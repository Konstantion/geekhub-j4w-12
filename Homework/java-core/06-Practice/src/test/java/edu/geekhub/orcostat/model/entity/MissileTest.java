package edu.geekhub.orcostat.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class MissileTest {
    @Test
    void default_missile_price_is_lada_vesta_sport() {
        final Missile missile = new Missile();

        assertEquals(30_000_000, missile.getPrice());
    }

    @Test
    void major_missile_price_can_differ_to_lada_vesta_sport() {
        final Missile missile = new Missile(30_000);

        assertEquals(30_000, missile.getPrice());
    }

    @Test
    void cannot_create_missile_with_negative_price() {
        Exception thrown = assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new Missile(-100)
        );

        assertEquals(
                "Price cannot be negative number",
                thrown.getMessage());
    }

    @Test
    void new_missile_has_date() {
        final Missile missile = new Missile();

        assertEquals(LocalDate.now(), missile.getDate());
    }

    @Test
    void new_missile_with_defined_date() {
        final Missile missile = new Missile(LocalDate.parse("2022-11-24"));

        LocalDate actualDate = LocalDate.of(2022, 11, 24);

        assertEquals(actualDate, missile.getDate());
    }

    @Test
    void new_missile_with_date_and_price() {
        final Missile missile = new Missile(1_000, LocalDate.parse("2022-11-24"));

        LocalDate actualDate = LocalDate.of(2022, 11, 24);
        int actualPrice = 1000;

        assertEquals(actualDate, missile.getDate());
        assertEquals(actualPrice, missile.getPrice());
    }
}
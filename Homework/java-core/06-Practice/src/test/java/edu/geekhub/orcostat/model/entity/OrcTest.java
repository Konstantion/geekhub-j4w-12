package edu.geekhub.orcostat.model.entity;

import edu.geekhub.orcostat.model.entity.Orc;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class OrcTest {
    @Test
    void default_orc_price_is_lada_vesta_sport() {
        final Orc orc = new Orc();

        assertEquals(10_000, orc.getPrice());
    }

    @Test
    void major_orc_price_can_differ_to_lada_vesta_sport() {
        final Orc orc = new Orc(30_000);

        assertEquals(30_000, orc.getPrice());
    }

    @Test
    void cannot_create_orc_with_negative_price() {
        Exception thrown = assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new Orc(-100)
        );

        assertEquals(
                "Price cannot be negative number",
                thrown.getMessage());
    }

    @Test
    void new_orc_has_date() {
        final Orc orc = new Orc();

        assertEquals(LocalDate.now(), orc.getDate());
    }

    @Test
    void new_orc_with_defined_date() {
        final Orc orc = new Orc(LocalDate.parse("2022-11-24"));

        LocalDate actualDate = LocalDate.of(2022, 11, 24);

        assertEquals(actualDate, orc.getDate());
    }

    @Test
    void new_orc_with_date_and_price() {
        final Orc orc = new Orc(1_000, LocalDate.parse("2022-11-24"));

        LocalDate actualDate = LocalDate.of(2022, 11, 24);
        int actualPrice = 1000;

        assertEquals(actualDate, orc.getDate());
        assertEquals(actualPrice, orc.getPrice());
    }
}
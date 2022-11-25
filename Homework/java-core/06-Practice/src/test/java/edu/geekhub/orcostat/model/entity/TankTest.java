package edu.geekhub.orcostat.model.entity;

import edu.geekhub.orcostat.model.collections.TrivialOrcCollection;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TankTest {
    @Test
    void can_build_tank() {
        new Tank();
    }

    @Test
    void default_tank_price_is_3_000_000() {
        Tank tank = new Tank();

        assertEquals(3_000_000, tank.getPrice());
    }

    @Test
    void default_tank_date_is_now() {
        Tank tank = new Tank();

        assertEquals(LocalDate.now(), tank.getDate());
    }

    @Test
    void empty_tank_has_zero_equipage() {
        Tank tank = new Tank();

        int count = tank.getEquipage().count();

        assertEquals(0, count);
    }

    @Test
    void tank_can_have_some_equipage() {
        TrivialOrcCollection equipage = new TrivialOrcCollection();
        equipage.add(new Orc());
        Tank tank = new Tank(equipage);

        int count = tank.getEquipage().count();

        assertEquals(1, count);
    }

    @Test
    void riding_tank_equipage_is_not_more_than_six_orcs() {
        TrivialOrcCollection equipage = new TrivialOrcCollection();
        for (int i = 0; i < 7; i++) {
            equipage.add(new Orc());
        }

        assertThrows(
                IllegalArgumentException.class,
                () -> new Tank(equipage)
        );
    }

    @Test
    void can_create_tank_with_specified_date() {
        Tank tank = new Tank(LocalDate.parse("2022-10-15"));

        LocalDate actualDate = LocalDate.of(2022, 10, 15);

        assertEquals(actualDate, tank.getDate());
    }

    @Test
    void can_create_tank_with_price() {
        int actualPrice = 10_000;
        Tank tank = new Tank(actualPrice);


        assertEquals(actualPrice, tank.getPrice());
    }

    @Test
    void can_create_tank_with_specified_date_and_price() {
        int actualPrice = 10_000;
        LocalDate actualDate = LocalDate.of(2022, 10, 15);

        Tank tank = new Tank(actualPrice, LocalDate.parse("2022-10-15"));

        assertEquals(actualDate, tank.getDate());
        assertEquals(actualPrice, tank.getPrice());
    }

    @Test
    void cannot_create_tank_with_equipage_equals_null() {
        TrivialOrcCollection equipage = null;

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> new Tank(equipage)
        );

        assertEquals("Equipage cannot be null", thrown.getMessage());
    }

}
package edu.geekhub.orcostat.repository;

import edu.geekhub.orcostat.beans.DatabaseBean;
import edu.geekhub.orcostat.model.collections.TrivialOrcCollection;
import edu.geekhub.orcostat.model.entity.Drone;
import edu.geekhub.orcostat.model.entity.Missile;
import edu.geekhub.orcostat.model.entity.Orc;
import edu.geekhub.orcostat.model.entity.Tank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrivialDatabaseTest {

    TrivialDatabase database;

    @BeforeEach
    void setUp() {
        database = DatabaseBean.INSTANCE.getPrototype();
    }

    @Test
    void add_orc() {
        Orc orc_1 = new Orc();
        Orc orc_2 = new Orc(1_000);

        database.save(orc_1);
        database.save(orc_2);
        Orc[] actualOrcs = database.getAllOrcs();
        Orc[] expectedOrcs = new Orc[]{
                orc_1,
                orc_2
        };

        assertArrayEquals(expectedOrcs, actualOrcs);
    }

    @Test
    void add_tank() {
        Tank tank_1 = new Tank();
        Tank tank_2 = new Tank(1_000);

        database.save(tank_1);
        database.save(tank_2);
        Tank[] actualTanks = database.getAllTanks();
        Tank[] expectedTanks = new Tank[]{
                tank_1,
                tank_2
        };

        assertArrayEquals(expectedTanks, actualTanks);
    }

    @Test
    void add_tank_with_equipage() {
        Orc orc_1 = new Orc();
        Orc orc_2 = new Orc(1_000);
        TrivialOrcCollection equipage = new TrivialOrcCollection();
        equipage.add(orc_1);
        equipage.add(orc_2);
        Tank tank_1 = new Tank(equipage);
        Tank tank_2 = new Tank(1_000);

        database.save(tank_1);
        database.save(tank_2);
        Tank[] actualTanks = database.getAllTanks();
        Tank[] expectedTanks = new Tank[]{
                tank_1,
                tank_2
        };
        Orc[] actualOrcs = database.getAllOrcs();
        Orc[] expectedOrcs = new Orc[]{
                orc_1,
                orc_2
        };

        assertArrayEquals(expectedTanks, actualTanks);
        assertArrayEquals(expectedOrcs, actualOrcs);
    }

    @Test
    void add_drone() {
        Drone drone_1 = new Drone();
        Drone drone_2 = new Drone(1_000_000);

        database.save(drone_1);
        database.save(drone_2);
        Drone[] actualDrones = database.getAllDrones();
        Drone[] expectedDrones = new Drone[]{
                drone_1,
                drone_2
        };

        assertArrayEquals(expectedDrones, actualDrones);
    }

    @Test
    void add_missile() {
        Missile missile_1 = new Missile();
        Missile missile_2 = new Missile(1_000_000);

        database.save(missile_1);
        database.save(missile_2);
        Missile[] actualMissiles = database.getAllMissiles();
        Missile[] expectedMissiles = new Missile[]{
                missile_1,
                missile_2
        };

        assertArrayEquals(expectedMissiles, actualMissiles);
    }

    @Test
    void add_orc_with_date() {
        Orc orc_1 = new Orc(1_000);
        Orc orc_2 = new Orc(LocalDate.parse("2022-04-10"));
        Orc orc_3 = new Orc(1_000);

        database.save(orc_1);
        database.save(orc_2);
        database.save(orc_3);
        Orc[] actualOrcs = database.getAllOrcsWithLocalDate(LocalDate.now());
        Orc[] expectedOrcs = new Orc[]{
                orc_1,
                orc_3
        };

        assertArrayEquals(expectedOrcs, actualOrcs);
    }

    @Test
    void add_tank_with_date() {
        Tank tank_1 = new Tank(1_000);
        Tank tank_2 = new Tank(LocalDate.parse("2022-04-10"));
        Tank tank_3 = new Tank(1_000);

        database.save(tank_1);
        database.save(tank_2);
        database.save(tank_3);
        Tank[] actualTanks = database.getAllTanksWithLocalDate(LocalDate.now());
        Tank[] expectedTanks = new Tank[]{
                tank_1,
                tank_3
        };

        assertArrayEquals(expectedTanks, actualTanks);
    }

    @Test
    void add_drone_with_date() {
        Drone drone_1 = new Drone(1_000);
        Drone drone_2 = new Drone(LocalDate.parse("2022-04-10"));
        Drone drone_3 = new Drone(1_000);

        database.save(drone_1);
        database.save(drone_2);
        database.save(drone_3);
        Drone[] actualDrones = database.getAllDronesWithLocalDate(LocalDate.now());
        Drone[] expectedDrones = new Drone[]{
                drone_1,
                drone_3
        };

        assertArrayEquals(expectedDrones, actualDrones);
    }

    @Test
    void add_missile_with_date() {
        Missile missile_1 = new Missile(1_000);
        Missile missile_2 = new Missile(LocalDate.parse("2022-04-10"));
        Missile missile_3 = new Missile(1_000);

        database.save(missile_1);
        database.save(missile_2);
        database.save(missile_3);
        Missile[] actualMissiles = database.getAllMissilesWithLocalDate(LocalDate.now());
        Missile[] expectedMissiles = new Missile[]{
                missile_1,
                missile_3
        };

        assertArrayEquals(expectedMissiles, actualMissiles);
    }
}
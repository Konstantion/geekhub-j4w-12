package edu.geekhub.orcostat.model.utils;

import edu.geekhub.orcostat.model.entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrivialArrayUtilTest {
    @Test
    void data_filter_works() {
        Orc orc_1 = new Orc();
        Orc orc_2 = new Orc();
        Orc orc_3 = new Orc();
        MilitaryLoss[] militaryLosses = new MilitaryLoss[]{
                orc_1,
                new Orc(LocalDate.parse("2022-02-10")),
                orc_2,
                orc_3,
                new Orc(LocalDate.parse("2022-03-01"))
        };

        MilitaryLoss[] actualFilteredArray = TrivialArrayUtil
                .filterMilitaryLossByLocalDate(militaryLosses, LocalDate.now());
        MilitaryLoss[] expectedFilteredArray = new MilitaryLoss[]{
                orc_1,
                orc_2,
                orc_3
        };

        assertArrayEquals(expectedFilteredArray, actualFilteredArray);
    }

    @Test
    void concat_arrays_work() {
        Orc orc_1 = new Orc();
        Orc orc_2 = new Orc();
        Tank tank_1 = new Tank();
        Tank tank_2 = new Tank();
        Drone drone_1 = new Drone();
        Missile missile_1 = new Missile();
        MilitaryLoss[] orcs = new MilitaryLoss[]{orc_1, orc_2};
        MilitaryLoss[] tanks = new MilitaryLoss[]{tank_1, tank_2};
        MilitaryLoss[] drones = new MilitaryLoss[]{drone_1};
        MilitaryLoss[] missiles = new MilitaryLoss[]{missile_1};
        MilitaryLoss[] empty = new MilitaryLoss[0];

        MilitaryLoss[] actualContactedArray = TrivialArrayUtil.concatAll(
                orcs,
                tanks,
                drones,
                missiles,
                empty);
        MilitaryLoss[] expectedContactedArray = new MilitaryLoss[]{
                orc_1, orc_2, tank_1, tank_2, drone_1, missile_1
        };

        assertArrayEquals(expectedContactedArray, actualContactedArray);
    }

}
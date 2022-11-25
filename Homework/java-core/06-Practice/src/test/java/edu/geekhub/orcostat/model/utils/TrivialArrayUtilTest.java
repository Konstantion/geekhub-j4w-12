package edu.geekhub.orcostat.model.utils;

import edu.geekhub.orcostat.model.entity.MilitaryLoss;
import edu.geekhub.orcostat.model.entity.Orc;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrivialArrayUtilTest {
    @Test
    void data_filter_works() {
        Orc orc_1 = new Orc();
        Orc orc_2 = new Orc();
        Orc orc_3 = new Orc();
        MilitaryLoss[] militaryLosses = new MilitaryLoss[] {
                orc_1,
                new Orc(LocalDate.parse("2022-02-10")),
                orc_2,
                orc_3,
                new Orc(LocalDate.parse("2022-03-01"))
        };

        MilitaryLoss[] actualFilteredArray = TrivialArrayUtil
                .filterMilitaryLossByLocalDate(militaryLosses, LocalDate.now());
        MilitaryLoss[] expectedFilteredArray = new MilitaryLoss[] {
                orc_1,
                orc_2,
                orc_3
        };

        assertArrayEquals(expectedFilteredArray, actualFilteredArray);
    }

}
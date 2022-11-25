package edu.geekhub.orcostat.model.utils;

import edu.geekhub.orcostat.model.entity.MilitaryLoss;

import java.time.LocalDate;
import java.util.Arrays;

public class TrivialArrayUtil {
    public static MilitaryLoss[] filterMilitaryLossByLocalDate(MilitaryLoss[] militaryLosses, LocalDate date) {
        MilitaryLoss[] filteredArray = new MilitaryLoss[militaryLosses.length];
        int filteredIndex = 0;

        for (MilitaryLoss militaryLoss : militaryLosses) {
            if (militaryLoss.getDate().equals(date)) {
                filteredArray[filteredIndex] = militaryLoss;
                filteredIndex++;
            }
        }

        return Arrays.copyOf(filteredArray, filteredIndex);
    }
}

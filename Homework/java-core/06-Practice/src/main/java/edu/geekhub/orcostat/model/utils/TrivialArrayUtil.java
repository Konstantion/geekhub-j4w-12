package edu.geekhub.orcostat.model.utils;

import edu.geekhub.orcostat.model.entity.MilitaryLoss;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class TrivialArrayUtil {
    public static MilitaryLoss[] filterMilitaryLossByLocalDate(
            MilitaryLoss[] militaryLosses, LocalDate date) {
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

    public static MilitaryLoss[] concatAll(MilitaryLoss[] first, MilitaryLoss[]... rest) {
        int totalLength = first.length;
        for (MilitaryLoss[] array : rest) {
            totalLength += array.length;
        }
        MilitaryLoss[] result = Arrays.copyOf(first, totalLength , MilitaryLoss[].class);
        int offset = first.length;
        for (MilitaryLoss[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}

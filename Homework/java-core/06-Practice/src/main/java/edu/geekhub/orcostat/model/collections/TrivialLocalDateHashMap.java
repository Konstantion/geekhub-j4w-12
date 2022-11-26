package edu.geekhub.orcostat.model.collections;

import java.time.LocalDate;
import java.util.Arrays;

import static java.util.Objects.isNull;

public class TrivialLocalDateHashMap {
    private static final int MAX_HASH = 600_000;
    private LocalDateMoneyPair[] map = new LocalDateMoneyPair[MAX_HASH];

    private void add(LocalDateMoneyPair pair) {
        map[pair.getDate().hashCode() % map.length] = pair;
    }

    public boolean contains(LocalDateMoneyPair pair) {
        return !isNull(map[pair.getDate().hashCode() % map.length]);
    }

    public void checkAndAdd(LocalDateMoneyPair pair) {
        if (contains(pair)) {
            map[pair.getDate().hashCode() % map.length].addMoney(pair.getMoney());
        } else {
            add(pair);
        }
    }

    public LocalDateMoneyPair[] extractArray() {
        LocalDateMoneyPair[] extractedArray = new LocalDateMoneyPair[MAX_HASH];
        int extractedIndex = 0;
        for (LocalDateMoneyPair pair : map) {
            if (!isNull(pair)) {
                extractedArray[extractedIndex] = pair;
                extractedIndex++;
            }
        }
        return Arrays.copyOf(extractedArray, extractedIndex);
    }
}

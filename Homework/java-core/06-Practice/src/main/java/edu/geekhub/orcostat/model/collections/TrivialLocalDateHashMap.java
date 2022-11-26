package edu.geekhub.orcostat.model.collections;

import java.util.Arrays;

import static java.util.Objects.isNull;

public class TrivialLocalDateHashMap {
    private static final int MAX_HASH = 600_000;
    private LocalDateLongPair[] map = new LocalDateLongPair[MAX_HASH];

    private void add(LocalDateLongPair pair) {
        map[pair.getDate().hashCode() % map.length] = pair;
    }

    public boolean contains(LocalDateLongPair pair) {
        return !isNull(map[pair.getDate().hashCode() % map.length]);
    }

    public void checkAndAdd(LocalDateLongPair pair) {
        if (contains(pair)) {
            map[pair.getDate().hashCode() % map.length].addValue(pair.getValue());
        } else {
            add(pair);
        }
    }

    public LocalDateLongPair[] extractArray() {
        LocalDateLongPair[] extractedArray = new LocalDateLongPair[MAX_HASH];
        int extractedIndex = 0;
        for (LocalDateLongPair pair : map) {
            if (!isNull(pair)) {
                extractedArray[extractedIndex] = pair;
                extractedIndex++;
            }
        }
        return Arrays.copyOf(extractedArray, extractedIndex);
    }
}

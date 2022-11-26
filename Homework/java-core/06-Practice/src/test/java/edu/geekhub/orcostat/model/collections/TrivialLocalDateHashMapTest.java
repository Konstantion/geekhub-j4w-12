package edu.geekhub.orcostat.model.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrivialLocalDateHashMapTest {
    private TrivialLocalDateHashMap hashMap;

    @BeforeEach
    void setUp() {
        hashMap = new TrivialLocalDateHashMap();
    }

    @Test
    void extract_array_with_multiple_dates() {
        LocalDateLongPair pair_1 = new LocalDateLongPair(LocalDate.now(), 10000);
        LocalDateLongPair pair_2 = new LocalDateLongPair(LocalDate.now(), 10000);
        LocalDateLongPair pair_3 = new LocalDateLongPair(LocalDate.now(), 10000);
        LocalDateLongPair pair_4 = new LocalDateLongPair(LocalDate.of(2010, 11, 1), 10000);
        LocalDateLongPair pair_5 = new LocalDateLongPair(LocalDate.of(2010, 11, 1), 10000);
        LocalDateLongPair pair_6 = new LocalDateLongPair(LocalDate.of(2013, 11, 1), 10000);
        hashMap.checkAndAdd(pair_1);
        hashMap.checkAndAdd(pair_2);
        hashMap.checkAndAdd(pair_3);
        hashMap.checkAndAdd(pair_4);
        hashMap.checkAndAdd(pair_5);
        hashMap.checkAndAdd(pair_6);

        LocalDateLongPair[] actualArray = hashMap.extractArray();
        LocalDateLongPair[] expectedArray = new LocalDateLongPair[]{
                new LocalDateLongPair(LocalDate.of(2010, 11, 1), 20000),
                new LocalDateLongPair(LocalDate.of(2013, 11, 1), 10000),
                new LocalDateLongPair(LocalDate.now(), 30000)
        };

        assertArrayEquals(expectedArray, actualArray);
    }


}
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
        LocalDateMoneyPair pair_1 = new LocalDateMoneyPair(LocalDate.now(), 10000);
        LocalDateMoneyPair pair_2 = new LocalDateMoneyPair(LocalDate.now(), 10000);
        LocalDateMoneyPair pair_3 = new LocalDateMoneyPair(LocalDate.now(), 10000);
        LocalDateMoneyPair pair_4 = new LocalDateMoneyPair(LocalDate.of(2010, 11, 1), 10000);
        LocalDateMoneyPair pair_5 = new LocalDateMoneyPair(LocalDate.of(2010, 11, 1), 10000);
        LocalDateMoneyPair pair_6 = new LocalDateMoneyPair(LocalDate.of(2013, 11, 1), 10000);
        hashMap.checkAndAdd(pair_1);
        hashMap.checkAndAdd(pair_2);
        hashMap.checkAndAdd(pair_3);
        hashMap.checkAndAdd(pair_4);
        hashMap.checkAndAdd(pair_5);
        hashMap.checkAndAdd(pair_6);

        LocalDateMoneyPair[] actualArray = hashMap.extractArray();
        LocalDateMoneyPair[] expectedArray = new LocalDateMoneyPair[]{
                new LocalDateMoneyPair(LocalDate.of(2010, 11, 1), 20000),
                new LocalDateMoneyPair(LocalDate.of(2013, 11, 1), 10000),
                new LocalDateMoneyPair(LocalDate.now(), 30000)
        };

        assertArrayEquals(expectedArray, actualArray);
    }


}
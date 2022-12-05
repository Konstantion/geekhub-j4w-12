package edu.geekhub.homework.hw1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OddIndexIterableTest {
    private OddIndexIterable<Integer> oddIndexIterable;

    @BeforeEach
    void setUp() {
        oddIndexIterable = new OddIndexIterable<>();
    }

    @Test
    void oddIndexIterable_add_elements() {
        oddIndexIterable.add(1);
        oddIndexIterable.add(2);
        oddIndexIterable.add(3);
        oddIndexIterable.add(4);
        oddIndexIterable.add(5);

        OddIndexIterator<Integer> iterator = (OddIndexIterator<Integer>) oddIndexIterable.iterator();

        int expectedIterationCount = (int) Math.floor(5 / 2.0);
        int actualIterationCount = 0;
        while (iterator.hasNext()) {
            iterator.next();
            actualIterationCount++;
        }

        assertEquals(
                expectedIterationCount,
                actualIterationCount
        );
    }
}
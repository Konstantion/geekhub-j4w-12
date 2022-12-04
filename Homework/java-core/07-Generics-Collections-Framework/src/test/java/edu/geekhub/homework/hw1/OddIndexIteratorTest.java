package edu.geekhub.homework.hw1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class OddIndexIteratorTest {

    List<Integer> data;

    @BeforeEach
    void setUp() {
        data = new ArrayList<>();
    }

    @Test
    void iterator_hasNext_false_when_Empty() {
        OddIndexIterator<Integer> iterator = new OddIndexIterator<>(data);

        assertFalse(iterator.hasNext());
    }

    @Test
    void iterator_hasNext_true_when_not_empty() {
        data.add(10);
        OddIndexIterator<Integer> iterator = new OddIndexIterator<>(data);

        assertTrue(iterator.hasNext());
    }

    @Test
    void iterator_next_throws_NoSuchElementException_when_empty() {
        OddIndexIterator<Integer> iterator = new OddIndexIterator<>(data);

        assertThrows(
                NoSuchElementException.class,
                iterator::next
        );
    }

    @Test
    void iterator_next_return_value_when_not_empty() {
        data.add(1);
        data.add(2);
        data.add(3);
        OddIndexIterator<Integer> iterator = new OddIndexIterator<>(data);

        assertEquals(2, iterator.next());
    }

    @Test
    void iterator_next_return_only_elements_with_odd_indexes() {
        data.add(1);
        data.add(2);
        data.add(3);
        data.add(4);
        data.add(5);
        OddIndexIterator<Integer> iterator = new OddIndexIterator<>(data);

        List<Integer> expectedElements = new ArrayList<>();
        expectedElements.add(2);
        expectedElements.add(4);
        List<Integer> actualElements = new ArrayList<>();
        while (iterator.hasNext()) {
            actualElements.add(iterator.next());
        }

        assertEquals(expectedElements, actualElements);
    }

}
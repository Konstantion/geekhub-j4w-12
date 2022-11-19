package edu.geekhub.homework.task3.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriviaQueueImpTest {

    private TrivialStringQueue queue;

    @BeforeEach
    void setUp() {
        queue = new TriviaQueueImp();
    }

    @Test
    void Given_EmptyQueue_When_GetSize_Than_ReturnZero() {
        assertEquals(0, queue.size());
    }

    @Test
    void Given_EmptyStack_When_Pop_Than_ThrowsIllegalArgumentException() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> queue.pop()
        );
    }

    @Test
    void Given_EmptyStack_When_Peak_Than_ThrowsIllegalArgumentException() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> queue.peak()
        );
    }

    @Test
    void Given_StringAsInput_When_Push_Than_SizeReturnOne() {
        queue.push("Some String");

        assertEquals(1, queue.size());
    }

    @Test
    void Given_StackWithTwoElements_When_Pop_Than_PeakReturnSecondElementAndSizeWillBeOne() {
        String first = "FIRST";
        String second = "SECOND";

        queue.push(first);
        queue.push(second);
        String popString = queue.pop();
        String peakString = queue.peak();

        assertEquals(popString, first);
        assertEquals(peakString, second);
        assertEquals(1, queue.size());
    }

    @Test
    @DisplayName("This test checks if stack automatically expands")
    void Given_OneHundredTenElements_When_Push_Than_ReturnOneHundredTenElements() {
        for (int i = 0; i < 110; i++) {
            queue.push(String.valueOf(i));
        }

        assertEquals(110, queue.size());
    }

    @Test
    void Given_ArrayOfSixStringAsInput_When_PushPopsTwoTimesPeak_Than_ReturnFourthString() {
        String[] input = new String[]{
                "FIRST",
                "SECOND",
                "THIRD",
                "FOURTH",
                "FIFTH",
                "SIXTH"
        };

        queue.push(input);
        String firstPop = queue.pop();
        String secondPop = queue.pop();

        assertEquals("FIRST", firstPop);
        assertEquals("SECOND", secondPop);
        assertEquals("THIRD", queue.peak());
    }

    @Test
    void Given_OneHundredElementsAsInput_When_PushAndPopWhileNotEmpty_Than_SizeReturnZero() {
        for (int i = 0; i < 100; i++) {
            queue.push(String.valueOf(i));
        }

        while (!queue.isEmpty()) {
            queue.pop();
        }

        assertEquals(0, queue.size());
    }

}
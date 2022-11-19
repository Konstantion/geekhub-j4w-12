package edu.geekhub.homework.task3.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrivialStackImpTest {

    private TrivialStringStack stack;

    @BeforeEach
    void setUp() {
        stack = new TrivialStackImp();
    }

    @Test
    void Given_EmptyStack_When_GetSize_Than_ThrowsIllegalArgumentException() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> stack.size()
        );
    }

    @Test
    void Given_EmptyStack_When_Pop_Than_ThrowsIllegalArgumentException() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> stack.pop()
        );
    }

    @Test
    void Given_EmptyStack_When_Peak_Than_ThrowsIllegalArgumentException() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> stack.peak()
        );
    }

    @Test
    void Given_StringAsInput_When_Push_Than_SizeReturnOne() {
        stack.push("Some String");

        assertEquals(1, stack.size());
    }

    @Test
    void Given_StackWithTwoElements_When_Pop_Than_PeakReturnFirstElementAndSizeWillBeOne() {
        String first = "FIRST";
        String second = "SECOND";

        stack.push(first);
        stack.push(second);
        String popString = stack.pop();
        String peakString = stack.peak();

        assertEquals(popString, second);
        assertEquals(peakString, first);
        assertEquals(1, stack.size());
    }

    @Test
    @DisplayName("This test checks if stack automatically expands")
    void Given_OneHundredTenElements_When_Push_Than_ReturnOneHundredTenElements() {
        for(int i = 0; i < 110; i++) {
            stack.push(String.valueOf(i));
        }

        assertEquals(110, stack.size());
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

        stack.push(input);
        String firstPop = stack.pop();
        String secondPop = stack.pop();

        assertEquals("SIXTH", firstPop);
        assertEquals("FIFTH", secondPop);
        assertEquals("FOURTH", stack.peak());
    }

}
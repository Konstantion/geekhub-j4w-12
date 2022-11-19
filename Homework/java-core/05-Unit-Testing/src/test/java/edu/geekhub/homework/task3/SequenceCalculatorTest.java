package edu.geekhub.homework.task3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static edu.geekhub.homework.task3.ArithmeticOperation.*;
import static org.junit.jupiter.api.Assertions.*;

class SequenceCalculatorTest {

    SequenceCalculator sequenceCalculator;
    final String standardInput = "1, 2, 3, 4";
    final String emptyString = " ";
    final String someRandomWord = "Some random words :)";

    @BeforeEach
    void setUp() {
        sequenceCalculator = new SequenceCalculator();
    }

    @Test
    void Given_NullAsInput_When_Calculate_Than_ThrowNullPointerException() {
        assertThrowsExactly(
                NullPointerException.class,
                () -> sequenceCalculator.calculate(null, ADDITION)
        );
    }

    @Test
    void Given_EmptyStringAsInput_When_Calculate_Than_ThrowIllegalArgumentException() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> sequenceCalculator.calculate(emptyString, ADDITION)
        );
    }

    @Test
    void Given_NullAsOperator_When_Calculate_Than_ThrowNullPointerException() {
        assertThrowsExactly(
                NullPointerException.class,
                () -> sequenceCalculator.calculate(standardInput, null)
        );
    }

    @Test
    void Given_StringWithoutSequenceAsInput_When_Calculate_Than_ThrowIllegalArgumentException() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> sequenceCalculator.calculate(someRandomWord, ADDITION)
        );
    }

    @Test
    void Given_SequenceWithZeroAndOperationDivisionAsInput_When_Calculate_Than_ThrowIllegalArgumentException() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> sequenceCalculator.calculate(standardInput + ", 0", DIVISION)
        );
    }

    @Test
    void Given_CorrectSequenceAsInput_When_Calculate_Addition_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate(standardInput, ADDITION);

        double expectedDouble = 1 + 2 + 3 + 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_CorrectSequenceAsInput_When_Calculate_Subtraction_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate(standardInput, SUBTRACTION);

        double expectedDouble = 1 - 2 - 3 - 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_CorrectSequenceAsInput_When_Calculate_Multiplication_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate(standardInput, MULTIPLICATION);

        double expectedDouble = 2 * 3 * 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_CorrectSequenceAsInput_When_Calculate_Division_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate(standardInput, DIVISION);

        double expectedDouble = 1.0 / 2 / 3 / 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_CorrectSequenceWithSpacesAsInput_When_Calculate_Addition_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("1  ,  2  ,3      ,4    ", ADDITION);

        double expectedDouble = 1 + 2 + 3 + 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_CorrectSequenceWithSpacesAsInput_When_Calculate_Subtraction_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("   1  ,  2  , 3      ,4    ", SUBTRACTION);

        double expectedDouble = 1 - 2 - 3 - 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_CorrectSequenceWithSpacesAsInput_When_Calculate_Multiplication_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("1  ,  2  ,3  ,    4    "
                , MULTIPLICATION);

        double expectedDouble = 2 * 3 * 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_CorrectSequenceWithSpacesAsInput_When_CalculateCalculate_Division_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("1  ,   2   ,  3      ,4    ", DIVISION);

        double expectedDouble = 1.0 / 2 / 3 / 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_CorrectSequenceWithSpecialAsInput_When_Calculate_Addition_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("1 %^#$ ,()()+*+  2 !@ ,3 ~^ ^^ ,4    "
                , ADDITION);

        double expectedDouble = 1 + 2 + 3 + 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_CorrectSequenceWithSpecialAsInput_When_Calculate_Subtraction_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("1 %^#$ ,()()+*+  2 !@ ,3 ~^ ^^ ,4    "
                , SUBTRACTION);

        double expectedDouble = 1 - 2 - 3 - 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_CorrectSequenceWithSpecialAsInput_When_Calculate_Multiplication_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("1 %^#$ ,()()+*+  2 !@ ,3 ~^ ^^ ,4    "
                , MULTIPLICATION);

        double expectedDouble = 2 * 3 * 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_CorrectSequenceWithSpecialAsInput_When_Calculate_Division_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("1 %^#$ ,()()+*+  2 !@ ,3 ~^ ^^ ,4    "
                , DIVISION);

        double expectedDouble = 1.0 / 2 / 3 / 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_CorrectSequenceWithMinusSignsAsInput_When_Calculate_Addition_Then_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("-1 %^#$ ,()()+-*+  2 !@ ,3 ~^ ^^ ,-4    "
                , ADDITION);

        double expectedDouble = -1 + (-2) + 3 + (-4);

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    @DisplayName("Program should reduce two minus signs without numeric value " +
                 "example ---1 = -1 ")
    void Given_CorrectSequenceWithMultipleMinusSignsAsInput_When_Calculate_Addition_ThenReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("---1 %^#$ ,()( - )+ - *+  2 !@ ,-3 ~^ ^^ ,--4"
                , ADDITION);

        double expectedDouble = -1 + 2 - 3 + 4;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_SequenceWithOneValueAsInput_When_Calculate_Then_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("---1 %^#$ ()(  )+ "
                , DIVISION);

        double expectedDouble = -1;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_SequenceWithOddNumbersAsInput_When_Calculate_Addition_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("1 %^#,3$ ()(, 5, ^%^&7,9 ,11 )+ "
                , ADDITION);

        double expectedDouble = 1 + 3 + 5 + 7 + 9 + 11;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_SequenceWithOddNumbersAsInput_When_Calculate_Subtraction_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("1 %^#,3$ ()(, 5, ^%^&7,9 ,11 )+ "
                , SUBTRACTION);

        double expectedDouble = 1 - 3 - 5 - 7 - 9 - 11;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_SequenceWithOddNumbersAsInput_When_Calculate_Multiplication_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("1 %^#,3$ ()(, 5, ^%^&7,9 ,11 )+ "
                , MULTIPLICATION);

        double expectedDouble = 3 * 5 * 7 * 9 * 11;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_SequenceWithOddNumbersAsInput_When_Calculate_Division_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("1 %^#,3$ ()(, 5, ^%^&7,9 ,11 )+ "
                , DIVISION);

        double expectedDouble = 1.0 / 3 / 5 / 7 / 9 / 11;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_SequenceWithEvenNumbersAsInput_When_Calculate_Addition_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("2 %^#,4$ ()(, 6, ^%^&8,10 ,12 )+ "
                , ADDITION);

        double expectedDouble = 2 + 4 + 6 + 8 + 10 + 12;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_SequenceWithEvenNumbersAsInput_When_Calculate_Subtraction_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("2 %^#,4$ ()(, 6, ^%^&8,10 ,12 )+ "
                , SUBTRACTION);

        double expectedDouble = 2 - 4 - 6 - 8 - 10 - 12;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_SequenceWithEvenNumbersAsInput_When_Calculate_Multiplication_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("2 %^#,4$ ()(, 6, ^%^&8,10 ,12 )+ "
                , MULTIPLICATION);

        double expectedDouble = 2 * 4 * 6 * 8 * 10 * 12;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_SequenceWithEvenNumbersAsInput_When_Calculate_Division_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("2 %^#,4$ ()(, 6, ^%^&8,10 ,12 )+ "
                , DIVISION);

        double expectedDouble = 2.0 / 4 / 6 / 8 / 10 / 12;

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    void Given_SequenceOfBigValuesAsInput_When_Calculate_Multiplication_Than_ReturnDouble() {
        double actualDouble = sequenceCalculator.calculate("1_000_000_000, 10, 5"
                , MULTIPLICATION);

        double expectedDouble = 1_000_000_000.0 * 10 * 5;

        assertEquals(expectedDouble, actualDouble);
    }
}
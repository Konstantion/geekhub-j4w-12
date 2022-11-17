package edu.geekhub.homework.task1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeGameFieldTest {
    private TicTacToeGameField ticTacToeGameField;
    private static final int FIELD_INPUT_LENGTH = 9;

    @BeforeEach
    void setUp() {
        ticTacToeGameField = new TicTacToeGameField();
    }

    @Test
    void Given_NullAsFieldInput_When_GenerateField_Then_ThrowsNullPointerException() {
        String fieldInput = null;

        assertThrowsExactly(
                NullPointerException.class,
                () -> ticTacToeGameField.generateField(fieldInput)
        );
    }

    @Test
    void Given_EmptyStringAsFieldInput_When_GenerateField_Then_ThrowsIllegalArgumentException() {
        String fieldInput = "";

        IllegalArgumentException exception = assertThrowsExactly(
                IllegalArgumentException.class,
                () -> ticTacToeGameField.generateField(fieldInput)
        );

        String expectedMessage = "Cant process empty field state";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void Given_StringWithForbiddenLengthsFieldInput_When_GenerateField_Then_ThrowsIllegalArgumentException() {
        String fieldInput = "Long String";

        IllegalArgumentException exception = assertThrowsExactly(
                IllegalArgumentException.class,
                () -> ticTacToeGameField.generateField(fieldInput)
        );

        String expectedMessage = "Field length: " + fieldInput.length() +
                " is not equal allowed length: " + FIELD_INPUT_LENGTH;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void Given_StringWithForbiddenCharactersAsFieldInput_When_GenerateField_Then_ThrowsIllegalArgumentException() {
        String fieldInput = "^ZXC_123$";

        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> ticTacToeGameField.generateField(fieldInput)
        );
    }

    @Test
    void Given_ValidStringAsFieldInput_When_GenerateField_Then_ReturnGameBoardString() {
        String fieldInput = "XXO O XO ";
        String separator = System.lineSeparator();

        String expectedGameBoard =
                "+-----+" + separator +
                        "|X|X|O|" + separator +
                        "| |O| |" + separator +
                        "|X|O| |" + separator +
                        "+-----+";
        String actualGameBoard = ticTacToeGameField.generateField(fieldInput);


        assertEquals(expectedGameBoard, actualGameBoard);
    }

    @Test
    void Given_NullAsGameField_When_SaveFieldState_Then_ThrowsIllegalArgumentException() {
        String gameField = null;

        IllegalArgumentException exception = assertThrowsExactly(
                IllegalArgumentException.class,
                () -> ticTacToeGameField.saveFieldState(gameField)
        );

        String expectedMessage = "Cant process empty field state";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void Given_EmptyStringAsGameField_When_SaveFieldState_ThrowsIllegalArgumentException() {
        String gameField = "  ";

        IllegalArgumentException exception = assertThrowsExactly(
                IllegalArgumentException.class,
                () -> ticTacToeGameField.saveFieldState(gameField)
        );

        String expectedMessage = "Cant process empty field state";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void Given_StringWithForbiddenLengthsAsGameField_When_SaveFieldState_Then_ThrowsIllegalArgumentException() {
        String gameField = "Long String";

        IllegalArgumentException exception = assertThrowsExactly(
                IllegalArgumentException.class,
                () -> ticTacToeGameField.generateField(gameField)
        );

        String expectedMessage = "Field length: " + gameField.length() +
                " is not equal allowed length: " + FIELD_INPUT_LENGTH;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void Given_StringWithForbiddenCharactersAsGameField_When_SaveFieldState_Then_ThrowsIllegalArgumentException() {
        String separator = System.lineSeparator();
        String gameField = "+-@---+" + separator +
                "|X|0|1|" + separator +
                "| |O| |" + separator +
                "|X|O| |" + separator +
                "+--^--+";

        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> ticTacToeGameField.saveFieldState(gameField)
        );
    }

    @Test
    void Given_ValidStringAsGameField_When_SaveFieldState_Then_ReturnGameFieldStateString() {
        String separator = System.lineSeparator();
        String gameField =
                "+-----+" + separator +
                        "|O|X|O|" + separator +
                        "| |O|X|" + separator +
                        "|X| |X|" + separator +
                        "+-----+";

        String expectedGameBoard = "OXO OXX X";
        String actualGameBoard = ticTacToeGameField.saveFieldState(gameField);

        assertEquals(expectedGameBoard, actualGameBoard);
    }

    @Test
    void Given_StringThatWillBecomeEmptyAfterReplacesAsGameField_When_SaveFieldState_Then_TrowsIllegalArgumentException() {
        String gameField = "+-----+||||  ";

        IllegalArgumentException exception = assertThrowsExactly(
                IllegalArgumentException.class,
                () -> ticTacToeGameField.saveFieldState(gameField)
        );

        String expectedMessage = "Cant process empty field state";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void Given_StringThatWillBecomeForbiddenAsGameField_When_SaveFieldState_Then_TrowsIllegalArgumentException() {
        String gameField = "+-----+||||12XO2OXO";

        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> ticTacToeGameField.saveFieldState(gameField)
        );
    }

    @Test
    void Given_ValidStringWithoutSeparatorsGameField_When_SaveFieldState_Then_ReturnGameFieldStateString() {
        String gameField = "+-----+|O|O|O||X|X| ||X| |X|+-----+";

        String expectedGameBoard = "OOOXX X X";
        String actualGameBoard = ticTacToeGameField.saveFieldState(gameField);

        assertEquals(expectedGameBoard, actualGameBoard);
    }
}
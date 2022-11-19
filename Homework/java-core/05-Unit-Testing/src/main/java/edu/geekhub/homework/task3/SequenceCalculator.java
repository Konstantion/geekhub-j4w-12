package edu.geekhub.homework.task3;

import edu.geekhub.homework.task3.utils.TrivialStackImp;
import edu.geekhub.homework.task3.utils.TrivialStringStack;

import static edu.geekhub.homework.util.NotImplementedException.TODO_TYPE;
import static java.lang.Character.isDigit;
import static java.util.Objects.requireNonNull;

/**
 * Arithmetic operation calculator for a sequence of values<br/>
 * As real world example try to input: 1*2*3 in a Chrome address bar
 */
public class SequenceCalculator {
    private static final char COMMA_CHAR = ',';
    private static final char MINUS_CHAR = '-';

    /**
     * Takes an input extract valid integers and calculate them using selected operation.<br/>
     * <p>
     * Example of work:
     * <pre>
     *     input: 1, 2, 3, 4
     *     operation: '*'
     *     result: 1 * 2 * 3 * 4 = 24
     * </pre>
     *
     * @param input     that contains a comma - ',' separated characters
     * @param operation {@link ArithmeticOperation} that should be applied to input numbers
     * @return result of calculation
     */
    int calculate(String input, ArithmeticOperation operation) {

        validate(input);


        return TODO_TYPE();
    }


    private void validate(String input) {
        validatePresent(input);
        validateContainsSequence(input);
    }

    private void validatePresent(String input) {
        requireNonNull(input);

        if (input.isBlank()) {
            throw new IllegalArgumentException("Input should not be empty");
        }
    }

    private void validateContainsSequence(String input) {
        String[] inputLines = input.split(String.valueOf(COMMA_CHAR));
        for (String inputLine : inputLines) {
            if (!lineContainInteger(inputLine)) {
                throw new IllegalArgumentException(
                        "Input should contain integer sequence separated by ','"
                );
            }
        }
    }

    private boolean lineContainInteger(String line) {
        char[] lineChars = line.toCharArray();
        for (char lineChar : lineChars) {
            if (isDigit(lineChar)) {
                return true;
            }
        }
        return false;
    }
}

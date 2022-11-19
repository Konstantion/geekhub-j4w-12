package edu.geekhub.homework.task3;

import edu.geekhub.homework.task3.utils.TrivialStackImp;
import edu.geekhub.homework.task3.utils.TrivialStringStack;

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
    Long calculate(String input, ArithmeticOperation operation) {

        validate(input);

        TrivialStringStack stack =
                extractSequenceToStack(input);

        return calculateSequenceFromStack(stack, operation);
    }

    private Long calculateSequenceFromStack(TrivialStringStack stack, ArithmeticOperation operation) {
        Long result = 0L;
        StringBuilder temporaryResult = new StringBuilder();
        while (!stack.isEmpty()) {
            String element = stack.pop();
            if (Character.isDigit(element.charAt(0))) {
                if (!stack.isEmpty() && stack.peak().equals("-")) {
                    String sing = stack.pop();
                    temporaryResult.append(sing);
                }
                temporaryResult.append(element);
            } else if (element.equals("-") && stack.hasNext() && stack.peak().equals("-")) {
                stack.pop();
            }
            if (!temporaryResult.isEmpty()) {
                validateOperation(Integer.parseInt(temporaryResult.toString()), operation);
                result = doArithmeticOperation(result, Integer.parseInt(temporaryResult.toString()), operation);
                temporaryResult = new StringBuilder();
            }
        }
        return result;
    }

    private void validateOperation(int operand, ArithmeticOperation operation) {
    }

    private Long doArithmeticOperation(Long number, int operand, ArithmeticOperation operation) {
        return null;
    }

    private TrivialStringStack extractSequenceToStack(String sequence) {
        TrivialStringStack extractedSequenceStack = new TrivialStackImp();
        sequence = removeUnnecessaryCharacters(sequence);

        String[] sequenceLines = sequence.split(String.valueOf(COMMA_CHAR));
        for (String sequenceLine : sequenceLines) {

            StringBuilder numberBuilder = new StringBuilder();

            char[] sequenceLineChars = sequenceLine.toCharArray();

            for (char sequenceLineChar : sequenceLineChars) {
                if (isDigit(sequenceLineChar)) {
                    numberBuilder.append(sequenceLineChar);
                } else if (sequenceLineChar == MINUS_CHAR) {
                    extractedSequenceStack.push(String.valueOf(MINUS_CHAR));
                }
            }
            extractedSequenceStack.push(numberBuilder.toString());
        }

        return extractedSequenceStack;
    }

    private String removeUnnecessaryCharacters(String sequence) {
        char[] sequenceChars = sequence.toCharArray();

        for (char sequenceChar : sequenceChars) {
            if (sequence.contains(String.valueOf(sequenceChar))
                && !(sequenceChar == COMMA_CHAR || sequenceChar == MINUS_CHAR)
                && !Character.isDigit(sequenceChar)) {
                sequence = sequence
                        .replace(String.valueOf(sequenceChar), "");
            }
        }

        return sequence;
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

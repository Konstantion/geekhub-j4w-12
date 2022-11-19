package edu.geekhub.homework.task3;

import edu.geekhub.homework.task3.utils.TriviaQueueImp;

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
    Double calculate(String input, ArithmeticOperation operation) {

        validate(input);
        validate(operation);

        TriviaQueueImp queue =
                extractSequenceToQueue(input);

        return calculateSequenceFromQueue(queue, operation);
    }

    private Double calculateSequenceFromQueue(TriviaQueueImp queue, ArithmeticOperation operation) {
        double result = 0;
        StringBuilder temporaryResult = new StringBuilder();
        boolean firstElementOfSequence = true;
        while (!queue.isEmpty()) {

            String element = queue.pop();

            if (element.equals("-") && !queue.isEmpty()) {
                //Remove two minus in a row occasions
                if (queue.peak().equals("-")) {
                    queue.pop();
                } else if (Character.isDigit(queue.peak().charAt(0))) {

                    temporaryResult.append(element);
                    temporaryResult.append(queue.pop());
                }
            } else {
                if (Character.isDigit(element.charAt(0))) {

                    temporaryResult.append(element);
                }
            }
            if (!temporaryResult.isEmpty()) {

                validateOperation(Integer.parseInt(temporaryResult.toString()), operation);

                //Assign result first numeric element of sequence
                if (firstElementOfSequence) {

                    result = Long.parseLong(temporaryResult.toString());
                    firstElementOfSequence = false;

                } else {

                    result = doOperation(result, Integer.parseInt(temporaryResult.toString()), operation);

                }
                temporaryResult = new StringBuilder();
            }
        }
        return result;
    }

    private TriviaQueueImp extractSequenceToQueue(String sequence) {
        TriviaQueueImp extractedSequenceQueue = new TriviaQueueImp();

        sequence = removeUnnecessaryCharacters(sequence);

        String[] sequenceLines = sequence.split(String.valueOf(COMMA_CHAR));
        for (String sequenceLine : sequenceLines) {

            StringBuilder numberBuilder = new StringBuilder();

            char[] sequenceLineChars = sequenceLine.toCharArray();

            for (char sequenceLineChar : sequenceLineChars) {
                if (isDigit(sequenceLineChar)) {
                    numberBuilder.append(sequenceLineChar);
                } else if (sequenceLineChar == MINUS_CHAR) {
                    extractedSequenceQueue.push(String.valueOf(MINUS_CHAR));
                }
            }
            extractedSequenceQueue.push(numberBuilder.toString());
        }

        return extractedSequenceQueue;
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

    private void validateOperation(int operand, ArithmeticOperation operation) {
        if (operand == 0 && operation.equals(ArithmeticOperation.DIVISION)) {
            throw new IllegalArgumentException("You can't divide by zero");
        }
    }

    private Double doOperation(double number, int operand, ArithmeticOperation operation) {
        switch (operation) {
            case ADDITION -> number = number + operand;
            case SUBTRACTION -> number = number - operand;
            case DIVISION -> number = number / operand;
            case MULTIPLICATION -> number = number * operand;
        }
        return number;
    }

    private void validate(String input) {
        validatePresent(input);
        validateContainsSequence(input);
    }
    private void validate(ArithmeticOperation operation) {
        validatePresent(operation);
    }

    private void validatePresent(String input) {
        requireNonNull(input);

        if (input.isBlank()) {
            throw new IllegalArgumentException("Input should not be empty");
        }
    }
    private void validatePresent(ArithmeticOperation operation) {
        requireNonNull(operation);
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

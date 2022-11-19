package edu.geekhub.homework.task3;

import static edu.geekhub.homework.task3.ArithmeticOperation.*;

public class ApplicationStarter {
    public static void main(String[] args) {
        SequenceCalculator sequenceCalculator = new SequenceCalculator();

        String input = "Enter your sequence";

        System.out.println(sequenceCalculator.calculate(input, SUBTRACTION));
    }
}

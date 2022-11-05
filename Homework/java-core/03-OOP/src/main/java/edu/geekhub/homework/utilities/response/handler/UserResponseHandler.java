package edu.geekhub.homework.utilities.response.handler;

import static edu.geekhub.homework.utilities.messages.WrongInputMessages.MUST_BE_GREATER_THAN_ZERO;

import edu.geekhub.homework.figures.Square;
import edu.geekhub.homework.implementation.Figure;
import edu.geekhub.homework.utilities.checkers.FigureInputChecker;
import edu.geekhub.homework.utilities.messages.UserInteractionMessages;
import java.util.Scanner;


public class UserResponseHandler {
    private static Scanner scanner = new Scanner(System.in);

    public static Figure selectedFigureHandler(int index) {
        //todo: finish method
        return null;
    }

    public static Square generateSquare() {
        System.out.printf("You chose a square%n"
                + "Enter a length of the square side, console will read only numeric value: ");
        while (scanner.hasNext()) {
            double side = scanner.nextDouble();
            if (FigureInputChecker.isSideExist(side)) {
                System.out.printf("Successfully build square with side %f%n", side);
                return Square.builder()
                        .side(side)
                        .build();
            } else {
                System.out.println(UserInteractionMessages
                        .wrongInputMessage(MUST_BE_GREATER_THAN_ZERO));
            }
        }
        return null;
    }
}

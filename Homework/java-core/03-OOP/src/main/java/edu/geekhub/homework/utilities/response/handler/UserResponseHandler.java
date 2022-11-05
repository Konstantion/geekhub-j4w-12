package edu.geekhub.homework.utilities.response.handler;

import static edu.geekhub.homework.utilities.messages.WrongInputMessages.MUST_BE_GREATER_THAN_ZERO;
import static edu.geekhub.homework.utilities.messages.WrongInputMessages.WRONG_INPUT_FORMAT;

import edu.geekhub.homework.figures.Circle;
import edu.geekhub.homework.figures.Rectangle;
import edu.geekhub.homework.figures.Square;
import edu.geekhub.homework.figures.Triangle;
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
        System.out.printf(UserInteractionMessages.userChoiceMessage());
        double side = getDoubleSideFromConsole();
        System.out.printf("Successfully build square with side %f%n", side);
        return Square.builder()
                .side(side)
                .build();
    }

    public static Circle generateCircle() {
        System.out.printf(UserInteractionMessages.userChoiceMessage());
        double radius = getDoubleSideFromConsole();
        System.out.printf("Successfully build circle with radius %f%n", radius);
        return Circle.builder()
                .radius(radius)
                .build();
    }

    public static Rectangle generateRectangle() {
        System.out.printf(UserInteractionMessages.userChoiceMessage());
        System.out.printf("Enter first side length:%n");
        double height = getDoubleSideFromConsole();
        System.out.printf("Enter second side length:%n");
        double width = getDoubleSideFromConsole();
        System.out.printf("Successfully build rectangle with sides %f %f%n", height, width);
        return Rectangle.builder()
                .width(width)
                .height(height)
                .build();
    }

    public static Triangle generateTriangle() {
        System.out.printf(UserInteractionMessages.userChoiceMessage());
        double sideA = getDoubleSideFromConsole();
        double sideB = getDoubleSideFromConsole();
        double sideC = getDoubleSideFromConsole();
        System.out.printf("Successfully build triangle with sides %f %f %f%n", sideA, sideB, sideC);
        return Triangle.builder()
                .sideA(sideA)
                .sideB(sideB)
                .sideC(sideC)
                .build();
    }

    private static double getDoubleSideFromConsole() {
        while (scanner.hasNext()) {
            if (scanner.hasNextDouble()) {
                double radius = scanner.nextDouble();
                if (FigureInputChecker.isSideExist(radius)) {
                    scanner.nextLine();
                    return radius;
                } else {
                    System.out.printf(UserInteractionMessages
                            .wrongInputMessage(MUST_BE_GREATER_THAN_ZERO));
                }
            } else {
                System.out.printf(UserInteractionMessages
                        .wrongInputMessage(WRONG_INPUT_FORMAT));
                scanner.next();
            }
        }
        return Double.MIN_VALUE;
    }

    private UserResponseHandler() {
    }
}

package edu.geekhub.homework.utilities.response.handler;

import static edu.geekhub.homework.utilities.messages.FigureInputMessages.*;
import static edu.geekhub.homework.utilities.messages.WrongInputMessages.*;

import edu.geekhub.homework.figures.Circle;
import edu.geekhub.homework.figures.Rectangle;
import edu.geekhub.homework.figures.Square;
import edu.geekhub.homework.figures.Triangle;
import edu.geekhub.homework.implementation.Figure;
import edu.geekhub.homework.utilities.checkers.FigureInputChecker;
import edu.geekhub.homework.utilities.messages.UserInteractionMessages;

import java.util.Scanner;


public class UserResponseHandler {
    private static final Scanner scanner = new Scanner(System.in);

    public static Figure selectedFigureHandler(int index) {
        //todo: finish method
        return null;
    }

    public static Square generateSquare() {
        System.out.printf(UserInteractionMessages.userChoiceMessage(SQUARE, SQUARE_INPUT));
        double side = getDoubleSideFromConsole();
        System.out.println(SUCCESSFULLY_CREATED);
        return Square.builder()
                .side(side)
                .build();
    }

    public static Circle generateCircle() {
        System.out.printf(UserInteractionMessages.userChoiceMessage(CIRCLE, CIRCLE_INPUT));
        double radius = getDoubleSideFromConsole();
        System.out.println(SUCCESSFULLY_CREATED);
        return Circle.builder()
                .radius(radius)
                .build();
    }

    public static Rectangle generateRectangle() {
        System.out.printf(UserInteractionMessages.userChoiceMessage(RECTANGLE, RECTANGLE_INPUT));

        System.out.println(getSideMessage(1));
        double height = getDoubleSideFromConsole();

        System.out.println(getSideMessage(2));
        double width = getDoubleSideFromConsole();

        System.out.println(SUCCESSFULLY_CREATED);
        return Rectangle.builder()
                .width(width)
                .height(height)
                .build();
    }

    public static Triangle generateTriangle() {
        System.out.printf(UserInteractionMessages.userChoiceMessage(TRIANGLE, TRIANGLE_INPUT));
        double sideA;
        double sideB;
        double sideC;
        while (true) {

            System.out.println(getSideMessage(1));
            sideA = getDoubleSideFromConsole();

            System.out.println(getSideMessage(2));
            sideB = getDoubleSideFromConsole();

            System.out.println(getSideMessage(3));
            sideC = getDoubleSideFromConsole();

            boolean isTriangle = FigureInputChecker
                    .isTriangleExist(sideA, sideB, sideC);
            if (!isTriangle) {
                System.out.printf(UserInteractionMessages
                        .wrongInputMessage(MUST_BE_GREATER_THAT_THIRD));
            } else {
                System.out.println(SUCCESSFULLY_CREATED);
                return Triangle.builder()
                        .sideA(sideA)
                        .sideB(sideB)
                        .sideC(sideC)
                        .build();
            }
        }
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

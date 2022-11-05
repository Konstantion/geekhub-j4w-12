package edu.geekhub.homework.utilities.response.handler;

import static edu.geekhub.homework.utilities.messages.FigureInputMessages.*;
import static edu.geekhub.homework.utilities.messages.WrongInputMessages.*;

import edu.geekhub.homework.figures.Circle;
import edu.geekhub.homework.figures.Rectangle;
import edu.geekhub.homework.figures.Square;
import edu.geekhub.homework.figures.Triangle;
import edu.geekhub.homework.implementation.Figure;
import edu.geekhub.homework.utilities.checkers.ChooseShapeChecker;
import edu.geekhub.homework.utilities.checkers.FigureInputChecker;
import edu.geekhub.homework.utilities.messages.UserInteractionMessages;

import java.util.Scanner;


public class UserResponseHandler {
    private static final Scanner scanner = new Scanner(System.in);

    public static Figure selectFigureHandler(int index) {
        System.out.print(UserInteractionMessages.selectShapeMessage(index));
        Integer userInput = getSelectedFigureChoice();
        Figure figure = switch (userInput) {
            case 1 -> generateCircle();
            case 2 -> generateTriangle();
            case 3 -> generateSquare();
            case 4 -> generateRectangle();
            default -> null;
        };
        System.out.println(UserInteractionMessages.showFigureMessage(index, figure));
        return figure;
    }

    public static Square generateSquare() {
        System.out.print(UserInteractionMessages.userChoiceMessage(SQUARE, SQUARE_INPUT));
        double side = getDoubleSideFromConsole();
        System.out.println(SUCCESSFULLY_CREATED);
        return Square.builder()
                .side(side)
                .build();
    }

    public static Circle generateCircle() {
        System.out.print(UserInteractionMessages.userChoiceMessage(CIRCLE, CIRCLE_INPUT));
        double radius = getDoubleSideFromConsole();
        System.out.println(SUCCESSFULLY_CREATED);
        return Circle.builder()
                .radius(radius)
                .build();
    }

    public static Rectangle generateRectangle() {
        System.out.print(UserInteractionMessages.userChoiceMessage(RECTANGLE, RECTANGLE_INPUT));

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
        System.out.print(UserInteractionMessages.userChoiceMessage(TRIANGLE, TRIANGLE_INPUT));
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
                System.out.print(UserInteractionMessages
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
                    System.out.print(UserInteractionMessages
                            .wrongInputMessage(MUST_BE_GREATER_THAN_ZERO));
                }
            } else {
                System.out.print(UserInteractionMessages
                        .wrongInputMessage(WRONG_INPUT_FORMAT));
                scanner.next();
            }
        }
        return Double.MIN_VALUE;
    }

    private static Integer getSelectedFigureChoice() {
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                Integer userChoice = scanner.nextInt();
                if (ChooseShapeChecker.isPossibleChoice(userChoice)) {
                    scanner.nextLine();
                    return userChoice;
                } else {
                    System.out.print(UserInteractionMessages
                            .wrongInputMessage(MUST_BE_GREATER_THAN_ZERO));
                }
            } else {
                System.out.print(UserInteractionMessages
                        .wrongInputMessage(WRONG_INPUT_FORMAT));
                scanner.next();
            }
        }
        return Integer.MIN_VALUE;
    }

    private UserResponseHandler() {
    }
}

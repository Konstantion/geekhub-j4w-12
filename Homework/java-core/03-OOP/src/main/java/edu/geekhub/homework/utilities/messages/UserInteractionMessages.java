package edu.geekhub.homework.utilities.messages;

import edu.geekhub.homework.figures.comparators.FigureComparator;
import edu.geekhub.homework.implementation.Figure;
import java.util.Comparator;

public class UserInteractionMessages {
    public static String selectShapeMessage(int index) {
        return String.format(
                "You are selecting the %d shape:%n"
                + "Press 1 if you want to select CIRCLE%n"
                + "Press 2 if you want to select TRIANGLE%n"
                + "Press 3 if you want to select SQUARE%n"
                + "Press 4 if you want to select RECTANGLE%n",
                index
        );
    }

    public static String wrongInputMessage(String message) {
        return String.format(
                "Wrong input data: %S%n"
                + "Try again!%n",
                message
        );
    }

    public static String showFigureMessage(int index, Figure figure) {
        return String.format(
                "figure %d:%n"
                + "%s",
                index,
                figure.toString()
        );
    }

    public static String selectColorMessage() {
        return String.format(
                "You are selecting color of the shape:%n"
                + "Type the title of the color, or if you want it to be default press `ENTER`%n"
        );
    }

    public static String compareFiguresMessage(Figure figure1, Figure figure2,
                                               FigureComparator comparator, String parameter) {
        return String.format(
                "figure1's %s is %s figure2's %s%n",
                parameter,
                FigureComparator.convertComparatorResultToString(
                        comparator.compare(figure1, figure2)
                ),
                parameter
        );
    }

    private UserInteractionMessages() {

    }
}

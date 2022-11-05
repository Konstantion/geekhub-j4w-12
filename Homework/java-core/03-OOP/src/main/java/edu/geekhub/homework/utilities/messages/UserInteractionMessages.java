package edu.geekhub.homework.utilities.messages;

import static edu.geekhub.homework.utilities.messages.MessagesColors.*;

import edu.geekhub.homework.figures.comparators.FigureComparator;
import edu.geekhub.homework.implementation.Figure;
import edu.geekhub.homework.utilities.string.StringUtilities;

public class UserInteractionMessages {
    public static String selectShapeMessage(int index) {
        return String.format(
                ANSI_YELLOW
                + "You are selecting the %d shape:%n"
                + "Press 1 if you want to select CIRCLE%n"
                + "Press 2 if you want to select TRIANGLE%n"
                + "Press 3 if you want to select SQUARE%n"
                + "Press 4 if you want to select RECTANGLE%n"
                + ANSI_RESET,
                index
        );
    }

    public static String wrongInputMessage(String message) {
        return String.format(
                ANSI_RED
                + "Wrong input data: %S%n"
                + "Try again!%n"
                + ANSI_RESET,
                message
        );
    }

    public static String showFigureMessage(Figure figure, int index) {
        return String.format(
                ANSI_BLUE
                + "%n"
                + "figure %d:%n"
                + "%s"
                + ANSI_RESET,
                index,
                figure.toString()
        );
    }

    public static String selectColorMessage() {
        return String.format(
                ANSI_YELLOW
                + "You are selecting color of the shape:%n"
                + "Type the title of the color,"
                + " or if you want it to be default press `ENTER`%n"
                + ANSI_RESET
        );
    }

    public static String compareFiguresMessage(Figure figure1, Figure figure2,
                                               FigureComparator comparator, String parameter) {
        return String.format(
                ANSI_PURPLE
                + "figure1's %s is %s figure2's %s%n"
                + ANSI_RESET,
                parameter,
                FigureComparator.convertComparatorResultToString(
                        comparator.compare(figure1, figure2)
                ),
                parameter
        );
    }

    public static String textColorMeaning() {
        return String.format(
                ANSI_YELLOW
                + "[YELLOW] - Interaction with user%n"
                + ANSI_RED
                + "[RED] - Wrong input data%n"
                + ANSI_BLUE
                + "[BLUE] - Figure status%n"
                + ANSI_GREEN
                + "[GREEN] - Operation successfully done%n"
                + ANSI_PURPLE
                + "[PURPLE] - Comparison result%n"
                + ANSI_CYAN
                + "[CYAN] - Header"
                + ANSI_RESET
        );
    }

    public static String userChoiceMessage(String figure, String data) {
        return String.format(
                ANSI_YELLOW
                + "You chose a %s%n"
                + "Enter the %s, console will read only numeric value:%n"
                + ANSI_RESET,
                figure,
                data
        );
    }

    public static String headerMessage(String header) {
        return String.format(
                ANSI_CYAN
                + "%n"
                + "%S"
                + ANSI_RESET + "%n",
                StringUtilities.center(header, 60)

        );
    }

    private UserInteractionMessages() {

    }
}

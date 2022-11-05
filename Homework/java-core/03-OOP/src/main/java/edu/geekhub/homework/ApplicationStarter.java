package edu.geekhub.homework;

import edu.geekhub.homework.figures.comparators.AreaComparator;
import edu.geekhub.homework.figures.comparators.PerimeterComparator;
import edu.geekhub.homework.implementation.Figure;
import edu.geekhub.homework.utilities.messages.UserInteractionMessages;
import edu.geekhub.homework.utilities.response.handler.UserResponseHandler;

public class ApplicationStarter {
    private static final AreaComparator AREA_COMPARATOR = new AreaComparator();
    private static final PerimeterComparator PERIMETER_COMPARATOR = new PerimeterComparator();

    public static void main(String[] args) {

        System.out.println(UserInteractionMessages.textColorMeaning());
        System.out.println(UserInteractionMessages
                .headerMessage("Beginning of the program (read the post above)"));

        Figure[] figures = new Figure[2];
        for (int i = 0; i < 2; i++) {
            int index = i + 1;
            figures[i] = UserResponseHandler.selectFigureHandler(index);
            UserResponseHandler.selectColorHandler(figures[i], index);
        }

        System.out.println(UserInteractionMessages.headerMessage("List of figures"));

        for (int i = 0; i < 2; i++) {
            int index = i + 1;
            System.out.println(UserInteractionMessages.showFigureMessage(figures[i], index));
        }

        System.out.println(UserInteractionMessages.headerMessage("Comparison of to figures"));

        System.out.println(UserInteractionMessages.compareFiguresMessage(
                figures[0], figures[1],
                AREA_COMPARATOR, AREA_COMPARATOR.parameter
        ));
        System.out.println(UserInteractionMessages.compareFiguresMessage(
                figures[0], figures[1],
                PERIMETER_COMPARATOR, PERIMETER_COMPARATOR.parameter
        ));
    }
}

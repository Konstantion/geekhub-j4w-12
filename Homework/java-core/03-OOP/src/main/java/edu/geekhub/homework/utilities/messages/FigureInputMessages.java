package edu.geekhub.homework.utilities.messages;


import static edu.geekhub.homework.utilities.messages.MessagesColors.*;

public class FigureInputMessages {
    public static final String SQUARE = "square";
    public static final String TRIANGLE = "triangle";
    public static final String RECTANGLE = "rectangle";
    public static final String CIRCLE = "circle";
    public static final String SQUARE_INPUT = "square side length";
    public static final String CIRCLE_INPUT = "circle radius length";
    public static final String RECTANGLE_INPUT = "rectangle sides length in separated lines";
    public static final String TRIANGLE_INPUT = "triangle sides length in separated lines";
    public static final String SUCCESSFULLY_CREATED = ANSI_GREEN
                            + "Figure successfully created"
                            + ANSI_RESET;
    public static String getSideMessage(int index){
        return String.format(
                ANSI_YELLOW
                + "Enter %d side"
                + ANSI_RESET,
                index
        );
    }


    private FigureInputMessages() {

    }
}

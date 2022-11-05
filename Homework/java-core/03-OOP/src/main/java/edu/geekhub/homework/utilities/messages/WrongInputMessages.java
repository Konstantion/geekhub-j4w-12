package edu.geekhub.homework.utilities.messages;

public class WrongInputMessages {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String MUST_BE_GREATER_THAN_ZERO = "Side/Radius length must be greater that zero";
    public static final String MUST_BE_GREATER_THAT_THIRD = "Sum of two sides of a triangle"
            + " must be greater than the third side";
    public static final String NO_SUCH_COLOR = "No such color provided";
    public static final String WRONG_INPUT_FORMAT = "Wrong input format";

    private WrongInputMessages() {

    }
}

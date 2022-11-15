package edu.geekhub.utils.validation.messages;

public class ValidationMessagesGenerator {
    public static String cannotBeNull(ValidationParameter parameter) {
        return String.format(
                "%s cannot be null! [NULL]",
                parameter
        );
    }

    public static String mustBeUnique(ValidationParameter parameter, Object data) {
        return String.format(
                "%s must be unique! [%s]",
                parameter,
                data.toString()
        );
    }

    public static String mustBeValid(ValidationParameter parameter, Object data) {
        return String.format(
                "%s must be valid! [%s]",
                parameter,
                data
        );
    }

    public static String cannotBeEmpty(ValidationParameter parameter, Object data) {
        return String.format(
                "%s cannot be empty! [%s]",
                parameter,
                data
        );
    }

    public static String cannotContainSpaces(ValidationParameter parameter, Object data) {
        return String.format(
                "%s cannot contain spaces! [%s]",
                parameter,
                data
        );
    }

    public static String cannotContainSpecialCharacters(ValidationParameter parameter, ValidationParameter characters, Object data) {
        return String.format(
                "%s cannot contain special characters such as %s! [%s]",
                parameter,
                characters.getParameter(),
                data
        );
    }

    public static String mustOneWord(ValidationParameter parameter, Object data) {
        return String.format(
                "%s must be one word! [%s]",
                parameter,
                data
        );
    }

    public static String mustBeInLowercase(ValidationParameter parameter, Object data) {
        return String.format(
                "%s must be in lowercase! [%s]",
                parameter,
                data
        );
    }

    public static String mustBeTwoWordsSeparatedBySpace(ValidationParameter parameter, Object data) {
        return String.format(
                "%s must be two words separated by space! [%s]",
                parameter,
                data
        );
    }

    public static String mustBeWrittenInCamelCase(ValidationParameter parameter, Object data) {
        return String.format(
                "%s must be written in camel case! [%s]",
                parameter,
                data
        );
    }

    public static String mustContainOnlyLetters(ValidationParameter parameter, Object data) {
        return String.format(
                "%s must contain only letters! [%s]",
                parameter,
                data
        );
    }

    public static String mustBeOver(ValidationParameter parameter, String number, Object data) {
        return String.format(
                "%s must be over %s! [%s]",
                parameter,
                number,
                data
        );
    }

    public static String mustBeLess(ValidationParameter parameter, String number, Object data) {
        return String.format(
                "%s must be less than %s!",
                parameter,
                number,
                data
        );
    }

    public static String cannotBeLonger(ValidationParameter parameter, String number, Object data) {
        return String.format(
                "%s cannot be longer than %s symbols! [%s]",
                parameter,
                number,
                data
        );
    }

    public static String mustBeZeroOrBigger(ValidationParameter parameter, Object data) {
        return String.format(
                "%s must be zero or bigger! [%s]",
                parameter,
                data
        );
    }


    private ValidationMessagesGenerator() {

    }
}

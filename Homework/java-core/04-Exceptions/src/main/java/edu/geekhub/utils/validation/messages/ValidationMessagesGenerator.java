package edu.geekhub.utils.validation.messages;

public class ValidationMessagesGenerator {
    public static String cannotBeNull(ValidationParameter parameter) {
        return String.format(
                "%s cannot be null!"
                        + "%n",
                parameter
        );
    }

    public static String mustBeUnique(ValidationParameter parameter) {
        return String.format(
                "%s must be unique!"
                        + "%n",
                parameter
        );
    }

    public static String mustBeValid(ValidationParameter parameter) {
        return String.format(
                "%s must be valid!"
                        + "%n",
                parameter
        );
    }

    public static String cannotBeEmpty(ValidationParameter parameter) {
        return String.format(
                "%s cannot be empty!"
                        + "%n",
                parameter
        );
    }

    public static String cannotContainSpaces(ValidationParameter parameter) {
        return String.format(
                "%s cannot contain spaces!"
                        + "%n",
                parameter
        );
    }

    public static String cannotContainSpecialCharacters(ValidationParameter parameter, ValidationParameter characters) {
        return String.format(
                "%s cannot contain special characters such as %s!"
                        + "%n",
                parameter,
                characters
        );
    }

    public static String mustOneWord(ValidationParameter parameter) {
        return String.format(
                "%s must be one word!"
                        + "%n",
                parameter
        );
    }

    public static String mustBeInLowercase(ValidationParameter parameter) {
        return String.format(
                "%s must be in lowercase!"
                        + "%n",
                parameter
        );
    }

    public static String mustBeTwoWordsSeparatedBy(ValidationParameter parameter, ValidationParameter separator) {
        return String.format(
                "%s must be two words separated by %s!"
                        + "%n",
                parameter,
                separator
        );
    }

    public static String mustBeWrittenInCamelCase(ValidationParameter parameter) {
        return String.format(
                "%s must be written in camel case!"
                        + "%n",
                parameter
        );
    }

    public static String mustContainOnlyLetters(ValidationParameter parameter) {
        return String.format(
                "%s must contain only letters!"
                        + "%n",
                parameter
        );
    }

    public static String mustBeOver(ValidationParameter parameter, String number) {
        return String.format(
                "%s must be over %s!"
                        + "%n",
                parameter,
                number
        );
    }

    public static String mustBeLess(ValidationParameter parameter, String number) {
        return String.format(
                "%s must be less than %s!"
                        + "%n",
                parameter,
                number
        );
    }

    public static String cannotBeLonger(ValidationParameter parameter, String number) {
        return String.format(
                "%s cannot be longer than %s symbols!"
                        + "%n",
                parameter,
                number
        );
    }
    public static String mustBeZeroOrBigger(ValidationParameter parameter) {
        return String.format(
                "%s must be zero or bigger!"
                        + "%n",
                parameter
        );
    }


    private ValidationMessagesGenerator() {

    }
}

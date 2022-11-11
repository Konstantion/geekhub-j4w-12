package edu.geekhub.utils.messages.validation;

public class ValidationMessagesGenerator {
    public static String cannotBeNull(String parameter) {
        return String.format(
                "%s cannot be null!"
                        + "%n",
                parameter
        );
    }

    public static String mustBeUnique(String parameter) {
        return String.format(
                "%s must be unique!"
                        + "%n",
                parameter
        );
    }

    public static String mustBeValid(String parameter) {
        return String.format(
                "%s must be valid!"
                        + "%n",
                parameter
        );
    }

    public static String cannotBeEmpty(String parameter) {
        return String.format(
                "%s cannot be empty!"
                        + "%n",
                parameter
        );
    }

    public static String cannotContainSpaces(String parameter) {
        return String.format(
                "%s cannot contain spaces!"
                        + "%n",
                parameter
        );
    }

    public static String cannotContainSpecialCharacters(String parameter, String characters) {
        return String.format(
                "%s cannot contain special characters such as %s!"
                        + "%n",
                parameter,
                characters
        );
    }

    public static String mustOneWord(String parameter) {
        return String.format(
                "%s must be one word!"
                        + "%n",
                parameter
        );
    }

    public static String mustBeInLowercase(String parameter) {
        return String.format(
                "%s must be in lowercase!"
                        + "%n",
                parameter
        );
    }

    public static String mustBeTwoWordsSeparatedBy(String parameter, String separator) {
        return String.format(
                "%s must be two words separated by %s!"
                        + "%n",
                parameter,
                separator
        );
    }

    public static String mustBeWrittenInCamelCase(String parameter) {
        return String.format(
                "%s must be written in camel case!"
                        + "%n",
                parameter
        );
    }

    public static String mustContainOnlyLetters(String parameter) {
        return String.format(
                "%s must contain only letters!"
                        + "%n",
                parameter
        );
    }

    public static String mustBeOver(String parameter, String number) {
        return String.format(
                "%s must be over %s!"
                        + "%n",
                parameter,
                number
        );
    }

    public static String mustBeLess(String parameter, String number) {
        return String.format(
                "%s must be less than %s!"
                        + "%n",
                parameter,
                number
        );
    }

    public static String cannotBeLonger(String parameter, String number) {
        return String.format(
                "%s cannot be longer than %s symbols!"
                        + "%n",
                parameter,
                number
        );
    }
    public static String mustBeZeroOrBigger(String parameter) {
        return String.format(
                "%s must be zero or bigger!"
                        + "%n",
                parameter
        );
    }


    private ValidationMessagesGenerator() {

    }
}

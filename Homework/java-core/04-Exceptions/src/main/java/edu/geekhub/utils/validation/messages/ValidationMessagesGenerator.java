package edu.geekhub.utils.validation.messages;

public class ValidationMessagesGenerator {
    public static String cannotBeNull(ValidationParameter parameter) {
        return String.format(
                "%s cannot be null!",
                parameter
        );
    }

    public static String mustBeUnique(ValidationParameter parameter) {
        return String.format(
                "%s must be unique!",
                parameter
        );
    }

    public static String mustBeValid(ValidationParameter parameter) {
        return String.format(
                "%s must be valid!",
                parameter
        );
    }

    public static String cannotBeEmpty(ValidationParameter parameter) {
        return String.format(
                "%s cannot be empty!",
                parameter
        );
    }

    public static String cannotContainSpaces(ValidationParameter parameter) {
        return String.format(
                "%s cannot contain spaces!",
                parameter
        );
    }

    public static String cannotContainSpecialCharacters(ValidationParameter parameter, ValidationParameter characters) {
        return String.format(
                "%s cannot contain special characters such as %s!",
                parameter,
                characters
        );
    }

    public static String mustOneWord(ValidationParameter parameter) {
        return String.format(
                "%s must be one word!",
                parameter
        );
    }

    public static String mustBeInLowercase(ValidationParameter parameter) {
        return String.format(
                "%s must be in lowercase!",
                parameter
        );
    }

    public static String mustBeTwoWordsSeparatedBySpace(ValidationParameter parameter) {
        return String.format(
                "%s must be two words separated by space!",
                parameter
        );
    }

    public static String mustBeWrittenInCamelCase(ValidationParameter parameter) {
        return String.format(
                "%s must be written in camel case!",
                parameter
        );
    }

    public static String mustContainOnlyLetters(ValidationParameter parameter) {
        return String.format(
                "%s must contain only letters!",
                parameter
        );
    }

    public static String mustBeOver(ValidationParameter parameter, String number) {
        return String.format(
                "%s must be over %s!",
                parameter,
                number
        );
    }

    public static String mustBeLess(ValidationParameter parameter, String number) {
        return String.format(
                "%s must be less than %s!",
                parameter,
                number
        );
    }

    public static String cannotBeLonger(ValidationParameter parameter, String number) {
        return String.format(
                "%s cannot be longer than %s symbols!",
                parameter,
                number
        );
    }

    public static String mustBeZeroOrBigger(ValidationParameter parameter) {
        return String.format(
                "%s must be zero or bigger!",
                parameter
        );
    }


    private ValidationMessagesGenerator() {

    }
}

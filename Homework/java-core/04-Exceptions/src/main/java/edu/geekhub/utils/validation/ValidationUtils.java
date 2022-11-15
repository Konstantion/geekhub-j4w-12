package edu.geekhub.utils.validation;

import edu.geekhub.exceptions.ConnectionInterruptedException;
import edu.geekhub.exceptions.UserValidationException;
import edu.geekhub.models.User;
import edu.geekhub.utils.validation.messages.ValidationParameter;

import java.util.Objects;
import java.util.UUID;

import static edu.geekhub.utils.validation.messages.ValidationMessagesGenerator.*;
import static edu.geekhub.utils.validation.messages.ValidationParameter.SPECIFIC_CHARACTERS;
import static edu.geekhub.utils.validation.messages.ValidationParameter.SUCCESS_VALIDATION;


public interface ValidationUtils {

    Object isUserValid(User user) throws UserValidationException, ConnectionInterruptedException;

    default String isNotNull(Object object, ValidationParameter parameter) {
        if (Objects.isNull(object)) {
            return cannotBeNull(parameter);
        } else {
            return SUCCESS_VALIDATION.getParameter();
        }
    }

    default String isNotEmpty(String string, ValidationParameter parameter) {
        if (string.isEmpty()) {
            return cannotBeEmpty(parameter, string);
        } else {
            return SUCCESS_VALIDATION.getParameter();
        }
    }

    String isIdUnique(UUID id, User[] users, ValidationParameter parameter)
            throws UserValidationException;

    String isEmailUnique(String email, User[] users, ValidationParameter parameter)
            throws UserValidationException;

    String isUsernameUnique(String username, User[] users, ValidationParameter parameter)
            throws UserValidationException;

    default String isEmailValid(String email, ValidationParameter parameter) {
        String badCharacters = "!#$%^&*()-/~[]`@";
        try {
            String name = email.substring(0, email.indexOf('@'));
            String domain = email.substring(email.indexOf('@') + 1);
            if (badCharacters.contains(String.valueOf(name.charAt(0))) ||
                    badCharacters.contains(String.valueOf(domain.charAt(domain.length() - 1)))
            ) {
                return mustBeValid(parameter, email);
            }
        } catch (Exception e) {
            return mustBeValid(parameter, email);
        }
        return SUCCESS_VALIDATION.getParameter();
    }

    default String isWithoutCharacters(String input, ValidationParameter parameter) {
        CharSequence specificCharacters = SPECIFIC_CHARACTERS.getParameter();
        if (input.contains(specificCharacters)) {
            return cannotContainSpecialCharacters(parameter, SPECIFIC_CHARACTERS, input);
        } else {
            return SUCCESS_VALIDATION.getParameter();
        }
    }

    default String isWithoutSpaces(String input, ValidationParameter parameter) {
        if (input.contains(" ")) {
            return cannotContainSpaces(parameter, input);
        } else {
            return SUCCESS_VALIDATION.getParameter();
        }
    }

    default String isOneWord(String input, ValidationParameter parameter) {

        if (input.contains(" ")) {
            return mustOneWord(parameter, input);
        } else {
            return SUCCESS_VALIDATION.getParameter();
        }
    }

    default String isOnlyLetters(String input, ValidationParameter parameter) {
        char[] chars = input.toCharArray();
        for (char ch : chars) {
            if (!Character.isLetter(ch) && !Character.isWhitespace(ch)) {
                return mustContainOnlyLetters(parameter, input);
            }
        }
        return SUCCESS_VALIDATION.getParameter();
    }

    default String isInLowercase(String input, ValidationParameter parameter) {
        char[] chars = input.toCharArray();
        for (char ch : chars) {
            if (!Character.isLowerCase(ch)) {
                return mustBeInLowercase(parameter, input);
            }
        }
        return SUCCESS_VALIDATION.getParameter();
    }

    default String isCamelCase(String input, ValidationParameter parameter) {
        String[] words = input.trim().split("\\s*");
        for (int i = 0; i < words.length; i++) {
            char[] wordChars = words[i].toCharArray();
            for (int j = 0; i < words.length; i++) {
                if (j != 0 && Character.isUpperCase(wordChars[j])) {
                    return mustBeWrittenInCamelCase(parameter, input);
                }
            }
        }
        return SUCCESS_VALIDATION.getParameter();
    }

    default String isTwoWordSeparatedBySpace(String input, ValidationParameter parameter) {
        if (input.split("\\s").length != 2) {
            return mustBeTwoWordsSeparatedBySpace(parameter, input);
        } else {
            return SUCCESS_VALIDATION.getParameter();
        }
    }

    default String isOverThan(Integer input, Integer than, ValidationParameter parameter) {
        if (input < than) {
            return mustBeOver(parameter, than.toString(), input);
        } else {
            return SUCCESS_VALIDATION.getParameter();
        }
    }

    default String isLessThan(Integer input, Integer than, ValidationParameter parameter) {
        if (input > than) {
            return mustBeLess(parameter, than.toString(), input);
        } else {
            return SUCCESS_VALIDATION.getParameter();
        }
    }

    default String isLonerThan(String input, Integer than, ValidationParameter parameter) {
        if (input.length() > than) {
            return cannotBeLonger(parameter, than.toString(), input);
        } else {
            return SUCCESS_VALIDATION.getParameter();
        }
    }

    default String isZeroOrBigger(Long input, ValidationParameter parameter) {
        if (input < 0) {
            return mustBeZeroOrBigger(parameter, input);
        } else {
            return SUCCESS_VALIDATION.getParameter();
        }
    }
}

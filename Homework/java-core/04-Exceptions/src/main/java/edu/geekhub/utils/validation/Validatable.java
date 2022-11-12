package edu.geekhub.utils.validation;

import edu.geekhub.exceptions.UserValidationException;
import edu.geekhub.models.User;
import edu.geekhub.models.request.Request;
import edu.geekhub.utils.validation.messages.ValidationParameter;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.geekhub.utils.validation.messages.ValidationMessagesGenerator.*;
import static edu.geekhub.utils.validation.messages.ValidationParameter.SPECIFIC_CHARACTERS;
import static edu.geekhub.utils.validation.patterns.PatternsEnum.*;


public interface Validatable {

    boolean isUserValid(User user, User[] users) throws UserValidationException;

    default boolean isNotNull(Object object, ValidationParameter parameter)
            throws UserValidationException {
        if (Objects.isNull(object)) {
            throw new UserValidationException(cannotBeNull(parameter));
        } else {
            return true;
        }
    }

    default boolean isNotEmpty(String string, ValidationParameter parameter)
            throws UserValidationException {
        if (string.isEmpty()) {
            throw new UserValidationException(cannotBeEmpty(parameter), string);
        } else {
            return true;
        }
    }

    boolean isIdUnique(UUID id, User[] users, ValidationParameter parameter)
            throws UserValidationException;

    boolean isEmailUnique(String email, User[] users, ValidationParameter parameter)
            throws UserValidationException;

    boolean isUsernameUnique(String username, User[] users, ValidationParameter parameter)
            throws UserValidationException;

    default boolean isEmailValid(String email, ValidationParameter parameter)
            throws UserValidationException {
        Pattern pattern = Pattern.compile(EMAIL.getPattern());
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new UserValidationException(mustBeValid(parameter), email);
        } else {
            return true;
        }
    }

    default boolean isWithoutCharacters(String input, ValidationParameter parameter)
            throws UserValidationException {
        Pattern pattern = Pattern.compile(SPECIAL_CHARACTERS.getPattern());
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            throw new UserValidationException(cannotContainSpecialCharacters(
                    parameter, SPECIFIC_CHARACTERS),
                    input
            );
        } else {
            return true;
        }
    }

    default boolean isWithoutSpaces(String input, ValidationParameter parameter)
            throws UserValidationException {
        if (input.contains(" ")) {
            throw new UserValidationException(cannotContainSpaces(parameter), input);
        } else {
            return true;
        }
    }

    default boolean isOneWord(String input, ValidationParameter parameter)
            throws UserValidationException {
        Pattern pattern = Pattern.compile(ONE_WORD.getPattern());
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            throw new UserValidationException(mustOneWord(parameter), input);
        } else {
            return true;
        }
    }

    default boolean isOnlyLetters(String input, ValidationParameter parameter)
            throws UserValidationException {
        Pattern pattern = Pattern.compile(ONLY_NOT_LETTERS.getPattern());
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            throw new UserValidationException(mustContainOnlyLetters(parameter), input);
        } else {
            return true;
        }
    }

    default boolean isInLowercase(String input, ValidationParameter parameter)
            throws UserValidationException {
        Pattern pattern = Pattern.compile(LOWERCASE.getPattern());
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            throw new UserValidationException(mustBeInLowercase(parameter), input);
        } else {
            return true;
        }
    }

    default boolean isCamelCase(String input, ValidationParameter parameter)
            throws UserValidationException {
        Pattern pattern = Pattern.compile(CAMEL_CASE.getPattern());
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            throw new UserValidationException(mustBeWrittenInCamelCase(parameter), input);
        } else {
            return true;
        }
    }

    default boolean isTwoWordSeparatedBySpace(String input, ValidationParameter parameter)
            throws UserValidationException {
        Pattern pattern = Pattern.compile(TWO_WORDS_SEPARATED_BY_SPACE.getPattern());
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            throw new UserValidationException(mustBeTwoWordsSeparatedBySpace(parameter), input);
        } else {
            return true;
        }
    }

    default boolean isOverThan(Integer input, Integer than, ValidationParameter parameter)
            throws UserValidationException {
        if (input < than) {
            throw new UserValidationException(mustBeOver(parameter, than.toString()), input);
        } else {
            return true;
        }
    }

    default boolean isLessThan(Integer input, Integer than, ValidationParameter parameter)
            throws UserValidationException {
        if (input > than) {
            throw new UserValidationException(mustBeLess(parameter, than.toString()), input);
        } else {
            return true;
        }
    }

    default boolean isLonerThan(String input, Integer than, ValidationParameter parameter)
            throws UserValidationException {
        if (input.length() > than) {
            throw new UserValidationException(cannotBeLonger(parameter, than.toString()), input);
        } else {
            return true;
        }
    }

    default boolean isZeroOrBigger(Long input, ValidationParameter parameter)
            throws UserValidationException {
        if (input < 0) {
            throw new UserValidationException(mustBeZeroOrBigger(parameter), input);
        } else {
            return true;
        }
    }
}

package edu.geekhub.utils.validation;

import edu.geekhub.exceptions.UserValidationException;
import edu.geekhub.models.User;
import edu.geekhub.models.request.Request;
import edu.geekhub.utils.validation.messages.ValidationParameter;

import java.util.Objects;
import java.util.UUID;

import static edu.geekhub.utils.validation.messages.ValidationMessagesGenerator.cannotBeNull;
import static edu.geekhub.utils.validation.messages.ValidationMessagesGenerator.mustBeUnique;
import static edu.geekhub.utils.validation.messages.ValidationParameter.*;

public class UserValidator implements Validatable {
    @Override
    public boolean isUserValid(User user, User[] users) throws UserValidationException {
        if (Objects.isNull(user)) {
            throw new UserValidationException(cannotBeNull(USER));
        }
        return isUserIdValid(user.getId(), users) &&
                isUserEmailValid(user.getEmail(), users) &&
                isUserUsernameValid(user.getUserName(), users) &&
                isUserFullNameValid(user.getFullName()) &&
                isUserAgeValid(user.getAge()) &&
                isUserNotesValid(user.getNotes()) &&
                isUserFollowersValid(user.getAmountOfFollowers());
    }

    private boolean isUserIdValid(UUID id, User[] users) throws UserValidationException {
        ValidationParameter parameter = USER_ID;
        return isNotNull(id, parameter) &&
                isIdUnique(id, users, parameter);
    }

    private boolean isUserEmailValid(String email, User[] users) throws UserValidationException {
        ValidationParameter parameter = USER_EMAIL;
        return isNotNull(email, parameter) &&
                isNotEmpty(email, parameter) &&
                isWithoutCharacters(email, parameter) &&
                isWithoutSpaces(email, parameter) &&
                isEmailValid(email, parameter) &&
                isEmailUnique(email, users, parameter);
    }

    private boolean isUserUsernameValid(String username, User[] users) throws UserValidationException {
        ValidationParameter parameter = USERNAME;
        return isNotNull(username, parameter) &&
                isNotEmpty(username, parameter) &&
                isWithoutCharacters(username, parameter) &&
                isOneWord(username, parameter) &&
                isInLowercase(username, parameter) &&
                isUsernameUnique(username, users, parameter);
    }

    private boolean isUserFullNameValid(String fullName) throws UserValidationException {
        ValidationParameter parameter = USER_FULL_NAME;
        return isNotNull(fullName, parameter) &&
                isNotEmpty(fullName, parameter) &&
                isTwoWordSeparatedBySpace(fullName, parameter) &&
                isCamelCase(fullName, parameter) &&
                isOnlyLetters(fullName, parameter);
    }

    private boolean isUserAgeValid(Integer age) throws UserValidationException {
        ValidationParameter parameter = USER_AGE;
        return isNotNull(age, parameter) &&
                isOverThan(age, Integer.parseInt(MIN_AGE.getParameter()), parameter) &&
                isLessThan(age, Integer.parseInt(MAX_AGE.getParameter()), parameter);
    }

    private boolean isUserNotesValid(String notes) throws UserValidationException {
        ValidationParameter parameter = USER_NOTES;
        return isNotNull(notes, parameter) &&
                isNotEmpty(notes, parameter) &&
                isLonerThan(notes, Integer.parseInt(NOTES_LENGTH.getParameter()), parameter);
    }

    private boolean isUserFollowersValid(Long followers) throws UserValidationException {
        ValidationParameter parameter = AMOUNT_OF_USER_FOLLOWERS;
        return isNotNull(followers, parameter) &&
                isZeroOrBigger(followers, parameter);
    }


    @Override
    public boolean isIdUnique(UUID id, User[] users, ValidationParameter parameter) throws UserValidationException {
        for (User user : users) {
            if (user.getId().equals(id)) {
                throw new UserValidationException(mustBeUnique(USER_ID), id);
            }
        }
        return true;
    }

    @Override
    public boolean isEmailUnique(String email, User[] users, ValidationParameter parameter) throws UserValidationException {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                throw new UserValidationException(mustBeUnique(USER_EMAIL), email);
            }
        }
        return true;
    }

    @Override
    public boolean isUsernameUnique(String username, User[] users, ValidationParameter parameter) throws UserValidationException {
        for (User user : users) {
            if (user.getUserName().equals(username)) {
                throw new UserValidationException(mustBeUnique(USERNAME), username);
            }
        }
        return true;
    }
}

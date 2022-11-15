package edu.geekhub.utils.validation;

import edu.geekhub.exceptions.ConnectionInterruptedException;
import edu.geekhub.models.User;
import edu.geekhub.storage.Repository;
import edu.geekhub.utils.datastructures.SimpleListImpl;
import edu.geekhub.utils.validation.messages.ValidationParameter;

import java.util.UUID;

import static edu.geekhub.utils.validation.messages.ValidationMessagesGenerator.mustBeUnique;
import static edu.geekhub.utils.validation.messages.ValidationParameter.*;

public class UserValidator implements ValidationUtils {
    private final Repository repository;
    private SimpleListImpl list;

    public UserValidator(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Object isUserValid(User user) throws ConnectionInterruptedException {
        list = new SimpleListImpl();
        User[] users = repository.tryToGetAll();
        isUserIdValid(user.getId(), users);
        isUserEmailValid(user.getEmail(), users);
        isUserUsernameValid(user.getUserName(), users);
        isUserFullNameValid(user.getFullName());
        isUserAgeValid(user.getAge());
        isUserNotesValid(user.getNotes());
        isUserFollowersValid(user.getAmountOfFollowers());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != SUCCESS_VALIDATION.getParameter()) {
                return list;
            }
        }
        return true;
    }

    private void isUserIdValid(UUID id, User[] users) {
        ValidationParameter parameter = USER_ID;
        list.add(isNotNull(id, parameter));
        if (list.lastAdded() != SUCCESS_VALIDATION.getParameter()) return;
        list.add(isIdUnique(id, users, parameter));
    }

    private void isUserEmailValid(String email, User[] users) {
        ValidationParameter parameter = USER_EMAIL;
        list.add(isNotNull(email, parameter));
        if (list.lastAdded() != SUCCESS_VALIDATION.getParameter()) return;
        list.add(isNotEmpty(email, parameter));
        list.add(isWithoutCharacters(email, parameter));
        list.add(isWithoutSpaces(email, parameter));
        list.add(isEmailValid(email, parameter));
        list.add(isEmailUnique(email, users, parameter));
    }

    private void isUserUsernameValid(String username, User[] users) {
        ValidationParameter parameter = USERNAME;
        list.add(isNotNull(username, parameter));
        if (list.lastAdded() != SUCCESS_VALIDATION.getParameter()) return;
        list.add(isNotEmpty(username, parameter));
        list.add(isWithoutCharacters(username, parameter));
        list.add(isOneWord(username, parameter));
        list.add(isInLowercase(username, parameter));
        list.add(isUsernameUnique(username, users, parameter));
    }

    private void isUserFullNameValid(String fullName) {
        ValidationParameter parameter = USER_FULL_NAME;
        list.add(isNotNull(fullName, parameter));
        if (list.lastAdded() != SUCCESS_VALIDATION.getParameter()) return;
        list.add(isNotEmpty(fullName, parameter));
        list.add(isTwoWordSeparatedBySpace(fullName, parameter));
        list.add(isCamelCase(fullName, parameter));
        list.add(isOnlyLetters(fullName, parameter));
    }

    private void isUserAgeValid(Integer age) {
        ValidationParameter parameter = USER_AGE;
        list.add(isNotNull(age, parameter));
        if (list.lastAdded() != SUCCESS_VALIDATION.getParameter()) return;
        list.add(isOverThan(age, Integer.parseInt(MIN_AGE.getParameter()), parameter));
        list.add(isLessThan(age, Integer.parseInt(MAX_AGE.getParameter()), parameter));
    }

    private void isUserNotesValid(String notes) {
        ValidationParameter parameter = USER_NOTES;
        list.add(isNotNull(notes, parameter));
        if (list.lastAdded() != SUCCESS_VALIDATION.getParameter()) return;
        list.add(isNotEmpty(notes, parameter));
        list.add(isLonerThan(notes, Integer.parseInt(NOTES_LENGTH.getParameter()), parameter));
    }

    private void isUserFollowersValid(Long followers) {
        ValidationParameter parameter = AMOUNT_OF_USER_FOLLOWERS;
        list.add(isNotNull(followers, parameter));
        if (list.lastAdded() != SUCCESS_VALIDATION.getParameter()) return;
        list.add(isZeroOrBigger(followers, parameter));
    }


    @Override
    public String isIdUnique(UUID id, User[] users, ValidationParameter parameter) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return mustBeUnique(USER_ID, id);
            }
        }
        return SUCCESS_VALIDATION.getParameter();
    }

    @Override
    public String isEmailUnique(String email, User[] users, ValidationParameter parameter) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return mustBeUnique(USER_EMAIL, email);
            }
        }
        return SUCCESS_VALIDATION.getParameter();
    }

    @Override
    public String isUsernameUnique(String username, User[] users, ValidationParameter parameter) {
        for (User user : users) {
            if (user.getUserName().equals(username)) {
                return mustBeUnique(USERNAME, username);
            }
        }
        return SUCCESS_VALIDATION.getParameter();
    }
}

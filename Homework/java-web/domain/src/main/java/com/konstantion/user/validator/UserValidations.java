package com.konstantion.user.validator;

import com.konstantion.utils.validator.ValidationsUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.konstantion.utils.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public record UserValidations() implements ValidationsUtil {
    private static final String EMAIL_UNICODE_CHARACTERS_REGEX = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
                                                                 + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
    private static final String PASSWORD_SPECIAL_CHARACTERS = "@#$%";

    private static final String PHONE_NUMBER_PATTERN = "^\\d{3}-\\d{3}-\\d{4}$";
    private static final String PHONE_NUMBER_PATTERN_ALTERNATIVE = "^\\d{10}$";

    public Optional<String> isEmailValid(String email) {
        if (isBlank(email)) {
            return Optional.of("email shouldn't be empty");
        }

        if (!Pattern.matches(EMAIL_UNICODE_CHARACTERS_REGEX, email)) {
            return Optional.of("email should match pattern <example@mail.com>");
        }

        return Optional.empty();
    }

    public Optional<String> isPasswordValid(String password) {
        List<String> errorList = new ArrayList<>();

        if (isBlank(password)) {
            return Optional.of("password shouldn't be empty");
        }

        if (password.length() < 4 || password.length() > 20) {
            errorList.add("password should be between 4 and 20 characters");
        }

        if (!hasAtLeastDigitCharacters(password, 1)) {
            errorList.add("password should contain at least one digit character");
        }

        if (!hasAtLeastLowerCaseCharacters(password, 1)) {
            errorList.add("password should contain at least one lower case character");
        }

        if (!hasAtLeastUpperCaseCharacters(password, 1)) {
            errorList.add("password should contain at least one upper case character");
        }

        if (!hasOneOfCharacters(password, PASSWORD_SPECIAL_CHARACTERS)) {
            errorList.add(String.format(
                    "password should contain at least at least one special character [%s]",
                    PASSWORD_SPECIAL_CHARACTERS
            ));
        }


        return listToOptionalString(errorList);
    }

    public Optional<String> isPasswordConfirmValid(String password, String passwordConfirmation) {
        List<String> errorList = new ArrayList<>();

        if (isBlank(passwordConfirmation)) {
            return Optional.of("confirmation password shouldn't be empty");
        }


        if (!password.equals(passwordConfirmation)) {
            errorList.add("confirmation password should be the same as password");
        }

        return listToOptionalString(errorList);
    }

    public Optional<String> isFirstnameValid(String firstname) {
        List<String> errorList = new ArrayList<>();

        if (isBlank(firstname)) {
            return Optional.of("firstname shouldn't be empty");
        }

        if (firstname.length() < 2 || firstname.length() > 12) {
            errorList.add("firstname should be between 3 and 12 characters");
        }

        if (hasMoreThanDigitCharacters(firstname, 0)) {
            errorList.add("firstname shouldn't contain numeric characters");
        }

        return listToOptionalString(errorList);
    }

    public Optional<String> isLastnameValid(String lastname) {
        List<String> errorList = new ArrayList<>();

        if (isBlank(lastname)) {
            return Optional.of("lastname shouldn't be empty");
        }

        if (lastname.length() < 2 || lastname.length() > 12) {
            errorList.add("lastname should be between 3 and 12 characters");
        }

        if (hasMoreThanDigitCharacters(lastname, 0)) {
            errorList.add("lastname shouldn't contain numeric characters");
        }

        return listToOptionalString(errorList);
    }

    public Optional<String> isLoginEmailValid(String email) {
        if (isBlank(email)) {
            return Optional.of("Email shouldn't be empty");
        }

        return Optional.empty();
    }

    public Optional<String> isLoginPasswordValid(String password) {
        if (isBlank(password)) {
            return Optional.of("Password shouldn't be empty");
        }

        return Optional.empty();
    }

    public Optional<String> isPhoneNumberValid(String phoneNumber) {
        List<String> errorList = new ArrayList<>();

        if (isBlank(phoneNumber)) {
            return Optional.of("Phone number shouldn't be empty");
        }

        if (!Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber)) {
            errorList.add("Invalid phone number format, should be |xxx-xxxx-xxx|");
        }

        return listToOptionalString(errorList);
    }

    public Optional<String> isUpdatePasswordValid(String password) {
        List<String> errorList = new ArrayList<>();

        if (isBlank(password)) {
            return Optional.empty();
        }

        if (password.length() < 4 || password.length() > 20) {
            errorList.add("password should be between 4 and 20 characters");
        }

        if (!hasAtLeastDigitCharacters(password, 1)) {
            errorList.add("password should contain at least one digit character");
        }

        if (!hasAtLeastLowerCaseCharacters(password, 1)) {
            errorList.add("password should contain at least one lower case character");
        }

        if (!hasAtLeastUpperCaseCharacters(password, 1)) {
            errorList.add("password should contain at least one upper case character");
        }

        if (!hasOneOfCharacters(password, PASSWORD_SPECIAL_CHARACTERS)) {
            errorList.add(String.format(
                    "password should contain at least at least one special character [%s]",
                    PASSWORD_SPECIAL_CHARACTERS
            ));
        }


        return listToOptionalString(errorList);
    }
}

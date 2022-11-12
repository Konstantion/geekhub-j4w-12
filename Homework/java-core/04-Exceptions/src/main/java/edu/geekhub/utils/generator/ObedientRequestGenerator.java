package edu.geekhub.utils.generator;

import edu.geekhub.models.request.Request;
import edu.geekhub.utils.RandomUtils;
import edu.geekhub.utils.RequestDataGenerator;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.UUID;

public class ObedientRequestGenerator implements RequestDataGenerator {
    private final String[] VALID_DOMAINS = {"gmail.com", "outlook.com", "aol.foo", "yahoo.bar", "protonmail.net", "foo.com", "bar.com"};
    private final String[] INVALID_DOMAINS = {"Gma1l.c0m", "@outl00ok.c0m", "a_l.c0m", "yahO_O.c0m", "p5otonm`iL.c0m", "7oo.c0m", "@@ba4.c0m"};
    private final String[] NAMES = {"James", "Robert", "John", "David", "William", "Joseph", "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Susan"};
    private String[] SURNAMES = {"Smith", "Johnson", "William", "Brown", "Garcia", "Miller"};
    private final char[] SPECIAL_CHARACTERS = "[(){}`//\\]\\['”]+".toCharArray();
    public boolean VALUE_CAN_BE_NULL = true;
    public boolean VALUE_CAN_BE_EMPTY = true;
    public boolean FULL_NAME_CAN_BE_INVALID = true;
    public boolean USERNAME_CAN_BE_INVALID = true;
    public boolean AGE_CAN_BE_INVALID = true;
    public boolean EMAIL_CAN_BE_INVALID = true;
    public boolean NOTE_CAN_BE_INVALID = true;
    public boolean ID_CAN_BE_INVALID = true;
    public boolean CONTAINS_SPECIFIC_CHARACTERS = true;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Request generate() {
        return null;
    }

    private UUID generateUUID() {
        if (ID_CAN_BE_INVALID) {
            boolean isNull = secureRandom.nextBoolean();
            if (isNull && VALUE_CAN_BE_NULL) {
                return null;
            }
            boolean isDuplicate = secureRandom.nextBoolean();
            if (isDuplicate) {
                return UUID.nameUUIDFromBytes("DUPLICATE :)".getBytes());
            }
        }
        return UUID.randomUUID();
    }

    public String generateEmail() {
        if (EMAIL_CAN_BE_INVALID) {
            boolean isNull = secureRandom.nextBoolean();
            if (isNull && VALUE_CAN_BE_NULL) {
                return null;
            }
            boolean isEmpty = secureRandom.nextBoolean();
            if (isEmpty && VALUE_CAN_BE_NULL) {
                return "";
            }
            boolean isDuplicate = secureRandom.nextBoolean();
            if (isDuplicate) {
                return "duplicate@gmail.com";
            }
            String email = "";
            boolean hasUsername = secureRandom.nextBoolean();
            if (hasUsername) {
                boolean isUsernameValid = secureRandom.nextBoolean();
                String username = getRandomString(secureRandom.nextInt(5, 20));
                if (!isUsernameValid) {
                    username = username.replace(
                            username.charAt(
                                    secureRandom.nextInt(0, username.length())
                            ),
                            SPECIAL_CHARACTERS[
                                    secureRandom.nextInt(0, SPECIAL_CHARACTERS.length)
                                    ]
                    );
                }
                email += username;
            }
            boolean hasSeparator = secureRandom.nextBoolean();
            if (hasSeparator) {
                email += "@";
            }
            boolean isValidDomain = secureRandom.nextBoolean();
            if (isValidDomain) {
                email += VALID_DOMAINS[
                        secureRandom.nextInt(0, VALID_DOMAINS.length)
                        ];
            } else {
                email += INVALID_DOMAINS[
                        secureRandom.nextInt(0, INVALID_DOMAINS.length)
                        ];
            }
            return email;
        }
        return getRandomString(secureRandom.nextInt(5, 20)) +
                "@" +
                VALID_DOMAINS[secureRandom.nextInt(0, VALID_DOMAINS.length)];
    }

    private String getRandomString(int size) {
        if (size <= 0) {
            return "";
        }
        int leftLimit = 97;
        int rightLimit = 122;
        return secureRandom.ints(leftLimit, rightLimit + 1)
                .limit(size)
                .collect(
                        StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append
                )
                .toString();
    }
}

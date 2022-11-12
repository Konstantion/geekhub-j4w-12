package edu.geekhub.utils.generator;

import edu.geekhub.models.User;
import edu.geekhub.models.request.Request;
import edu.geekhub.utils.RequestDataGenerator;

import java.security.SecureRandom;
import java.util.UUID;

public class ObedientRandomRequestGenerator implements RequestDataGenerator {
    private final String[] VALID_DOMAINS = {"gmail.com", "outlook.com",
            "aol.foo", "yahoo.bar", "protonmail.net", "foo.com", "bar.com"};
    private final String[] INVALID_DOMAINS = {"Gma1l.c0m", "@outl00ok.c0m",
            "a_l.c0m", "yahO_O.c0m", "p5otonm`iL.c0m", "7oo.c0m", "@@ba4.c0m"};
    private final String[] VALID_NAMES = {"James", "Robert", "John", "David",
            "William", "Joseph", "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Susan"};
    private final String[] VALID_SURNAMES = {"Smith", "Johnson", "William",
            "Brown", "Garcia", "Miller"};
    private boolean valueCanBeNull = true;
    private boolean valueCanBeEmpty = true;
    private boolean fullNameCanBeInvalid = true;
    private boolean usernameCanBeInvalid = true;
    private boolean ageCanBeInvalid = true;
    private boolean emailCanBeInvalid = true;
    private boolean noteCanBeInvalid = true;
    private boolean idCanBeInvalid = true;
    private boolean followersCanBeInvalid = true;
    private final SecureRandom secureRandom = new SecureRandom();

    private final GeneratorUtils utils = new GeneratorUtils(secureRandom);

    @Override
    public Request generate() {
        Object data = generateUser();

        return new Request(data);
    }

    private User generateUser() {
        return User.toBuilder()
                .withId(generateUUID())
                .withEmail(generateEmail())
                .withUserName(generateUsername())
                .withFullName(generateFullName())
                .withAge(generateAge())
                .withNotes(generateNotes())
                .withAmountOfFollowers(generateFollowers())
                .build();
    }

    private UUID generateUUID() {
        if (idCanBeInvalid) {
            boolean isNull = secureRandom.nextBoolean();
            if (isNull && valueCanBeNull) {
                return null;
            }
            boolean isDuplicate = secureRandom.nextBoolean();
            if (isDuplicate) {
                return UUID.nameUUIDFromBytes("DUPLICATE :)".getBytes());
            }
        }
        return UUID.randomUUID();
    }

    private String generateEmail() {
        if (emailCanBeInvalid) {
            boolean isNull = secureRandom.nextBoolean();
            if (isNull && valueCanBeNull) {
                return null;
            }
            boolean isEmpty = secureRandom.nextBoolean();
            if (isEmpty && valueCanBeEmpty) {
                return "";
            }
            boolean isDuplicate = secureRandom.nextBoolean();
            if (isDuplicate) {
                return "duplicate@gmail.com";
            }
            String email = "";
            boolean hasUsername = secureRandom.nextBoolean();
            if (hasUsername) {
                String username = utils.getRandomString(10);
                email = utils.randomlyAddSpecificCharacter(username);
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
        return utils.getRandomString(secureRandom.nextInt(5, 20)) +
                "@" +
                VALID_DOMAINS[secureRandom.nextInt(0, VALID_DOMAINS.length)];
    }

    private String generateUsername() {
        if (usernameCanBeInvalid) {
            boolean isNull = secureRandom.nextBoolean();
            if (isNull && valueCanBeNull) {
                return null;
            }
            boolean isEmpty = secureRandom.nextBoolean();
            if (isEmpty && valueCanBeEmpty) {
                return "";
            }
            boolean isDuplicate = secureRandom.nextBoolean();
            if (isDuplicate) {
                return "duplicated";
            }
            String username = utils.getRandomString(secureRandom.nextInt(5, 15));
            boolean hasSpace = secureRandom.nextBoolean();
            if (hasSpace) {
                username = utils.randomlyAddWhiteSpace(username);
            }
            boolean hasSpecificCharacters = secureRandom.nextBoolean();
            if (hasSpecificCharacters) {
                username = utils.randomlyAddSpecificCharacter(username);
            }
            boolean hasUpperCaseLetter = secureRandom.nextBoolean();
            if (hasUpperCaseLetter) {
                username = utils.randomlyToUpperCase(username);
            }
            return username;
        }
        return utils.getRandomString(10);
    }

    private String generateFullName() {
        if (fullNameCanBeInvalid) {
            boolean isNull = secureRandom.nextBoolean();
            if (isNull && valueCanBeNull) {
                return null;
            }
            boolean isEmpty = secureRandom.nextBoolean();
            if (isEmpty && valueCanBeEmpty) {
                return "";
            }
            boolean isDuplicate = secureRandom.nextBoolean();
            if (isDuplicate) {
                return "Duplicated Name";
            }
            String fullName = String.format(
                    "%s %s",
                    VALID_NAMES[secureRandom.nextInt(0, VALID_NAMES.length)],
                    VALID_SURNAMES[secureRandom.nextInt(0, VALID_SURNAMES.length)]
            );
            boolean containsSpace = secureRandom.nextBoolean();
            if (!containsSpace) {
                fullName = fullName.replaceAll("\\s", "");
            }
            boolean inCamelCase = secureRandom.nextBoolean();
            if (!inCamelCase) {
                fullName = utils.randomlyToUpperCase(fullName);
            }
            boolean containsOnlyLetters = secureRandom.nextBoolean();
            if (!containsOnlyLetters) {
                fullName = utils.randomlyAddNumber(fullName);
            }
            return fullName;
        }
        return String.format(
                "%s %s",
                VALID_NAMES[secureRandom.nextInt(0, VALID_NAMES.length)],
                VALID_SURNAMES[secureRandom.nextInt(0, VALID_SURNAMES.length)]
        );
    }

    private Integer generateAge() {
        if (ageCanBeInvalid) {
            boolean isNull = secureRandom.nextBoolean();
            if (isNull && valueCanBeNull) {
                return null;
            }
            boolean lowerOrGreater = secureRandom.nextBoolean();
            if (lowerOrGreater) {
                secureRandom.nextInt(-100, 18);
            } else {
                secureRandom.nextInt(100, 1000);
            }
        }
        return secureRandom.nextInt(18, 100);
    }

    private String generateNotes() {
        if (noteCanBeInvalid) {
            boolean isNull = secureRandom.nextBoolean();
            if (isNull && valueCanBeNull) {
                return null;
            }
            boolean isEmpty = secureRandom.nextBoolean();
            if (isEmpty && valueCanBeEmpty) {
                return "";
            }
            boolean longerThanMax = secureRandom.nextBoolean();
            if (longerThanMax) {
                return utils.getNote(secureRandom.nextInt(100)).repeat(20);
            }
        }
        return utils.getNote(secureRandom.nextInt(100));
    }

    private Long generateFollowers() {
        if (followersCanBeInvalid) {
            boolean isNull = secureRandom.nextBoolean();
            if (isNull) {
                return null;
            }
            boolean isLowerThanZero = secureRandom.nextBoolean();
            if (isLowerThanZero) {
                return -1L;
            }
        }
        return secureRandom.nextLong(0, 1000000000L);
    }

    public void setValueCanBeNull(boolean valueCanBeNull) {
        this.valueCanBeNull = valueCanBeNull;
    }

    public void setValueCanBeEmpty(boolean valueCanBeEmpty) {
        this.valueCanBeEmpty = valueCanBeEmpty;
    }

    public void setUsernameCanBeInvalid(boolean usernameCanBeInvalid) {
        this.usernameCanBeInvalid = usernameCanBeInvalid;
    }

    public void setAgeCanBeInvalid(boolean ageCanBeInvalid) {
        this.ageCanBeInvalid = ageCanBeInvalid;
    }

    public void setEmailCanBeInvalid(boolean emailCanBeInvalid) {
        this.emailCanBeInvalid = emailCanBeInvalid;
    }

    public void setNoteCanBeInvalid(boolean noteCanBeInvalid) {
        this.noteCanBeInvalid = noteCanBeInvalid;
    }

    public void setIdCanBeInvalid(boolean idCanBeInvalid) {
        this.idCanBeInvalid = idCanBeInvalid;
    }

    public void setFullNameCanBeInvalid(boolean fullNameCanBeInvalid) {
        this.fullNameCanBeInvalid = fullNameCanBeInvalid;
    }

    public void setFollowersCanBeInvalid(boolean followersCanBeInvalid) {
        this.followersCanBeInvalid = followersCanBeInvalid;
    }
}

package edu.geekhub.utils.generator;

import java.security.SecureRandom;

public class GeneratorUtils {
    private final SecureRandom secureRandom;
    private final char[] SPECIAL_CHARACTERS_ARRAY = "[(){}`//\\]\\['”]+".toCharArray();
    private final char[] UPPERCASE_LETTERS = "ABCDEFGHIGKLMNOPQRSTYVWXYZ".toCharArray();
    private final char[] NUMBERS = "1234567890".toCharArray();

    public GeneratorUtils(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    public String randomlyAddSpecificCharacter(String string) {
        boolean hasSpecificCharacters = secureRandom.nextBoolean();
        if (hasSpecificCharacters) {
            string = string.replace(
                    string.charAt(
                            secureRandom.nextInt(0, string.length())
                    ),
                    SPECIAL_CHARACTERS_ARRAY[
                            secureRandom.nextInt(0, SPECIAL_CHARACTERS_ARRAY.length)
                            ]
            );
        }
        return string;
    }

    public String randomlyAddWhiteSpace(String string) {
        boolean hasWhiteSpace = secureRandom.nextBoolean();
        if (hasWhiteSpace) {
            string = string.replace(
                    string.charAt(
                            secureRandom.nextInt(1, string.length() - 1)
                    ),
                    ' '
            );
        }
        return string;
    }

    public String randomlyToUpperCase(String string) {
        boolean hasUpperCase = secureRandom.nextBoolean();
        if (!hasUpperCase) {
            string = string.replace(
                    string.charAt(
                            secureRandom.nextInt(0, string.length())
                    ),
                    UPPERCASE_LETTERS[
                            secureRandom.nextInt(0, UPPERCASE_LETTERS.length)
                            ]
            );
        }
        return string;
    }

    public String randomlyAddNumber(String string) {
        boolean hasUpperCase = secureRandom.nextBoolean();
        if (!hasUpperCase) {
            string = string.replace(
                    string.charAt(
                            secureRandom.nextInt(0, string.length())
                    ),
                    NUMBERS[
                            secureRandom.nextInt(0, NUMBERS.length)
                            ]
            );
        }
        return string;
    }

    public String getRandomString(int size) {
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

    public String getNote(int index) {
        return UserNotesContainer.getNote(index);
    }

    private class UserNotesContainer {
        private static String[] notes = {
            "Ukraine’s glory has not yet died, nor her freedom,",
            "Upon us, my young brothers, fate shall yet smile.",
            "Our enemies will perish, like dew in the morning sun,",
            "And we too shall rule, brothers, in our own land."
        };

        public static String getNote(int index) {
            index = Math.abs(index) % notes.length;
            return notes[index];
        }

        private UserNotesContainer() {
        }
    }
}

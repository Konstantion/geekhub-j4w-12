package com.konstantion.utils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Ascii.isLowerCase;
import static com.google.common.base.Ascii.isUpperCase;
import static java.lang.Character.isDigit;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class StringUtils {
    private StringUtils() {

    }

    /**
     * @param sequence target sequence
     * @param count    count of specified characters
     * @return occurs >= count
     */
    public static boolean hasAtLeastDigitCharacters(CharSequence sequence, int count) {
        if (isBlank(sequence)) {
            return false;
        }
        int occurs = 0;
        char[] chars = sequence.toString().toCharArray();
        for (char ch : chars) {
            if (isDigit(ch)) {
                occurs++;
            }
        }

        return occurs >= count;
    }

    /**
     * @param sequence target sequence
     * @param count    count of specified characters
     * @return occurs > count
     */
    public static boolean hasMoreThanDigitCharacters(CharSequence sequence, int count) {
        if (isBlank(sequence)) {
            return false;
        }
        int occurs = 0;
        char[] chars = sequence.toString().toCharArray();
        for (char ch : chars) {
            if (isDigit(ch)) {
                occurs++;
            }
        }

        return occurs > count;
    }

    /**
     * @param sequence target sequence
     * @param count    count of specified characters
     * @return occurs <= count
     */
    public static boolean hasAtMostDigitCharacters(CharSequence sequence, int count) {
        if (isBlank(sequence)) {
            return false;
        }
        int occurs = 0;
        char[] chars = sequence.toString().toCharArray();
        for (char ch : chars) {
            if (isDigit(ch)) {
                occurs++;
            }
        }

        return occurs <= count;
    }

    /**
     * @param sequence target sequence
     * @param count    count of specified characters
     * @return occurs < count
     */
    public static boolean hasFewerThanDigitCharacters(CharSequence sequence, int count) {
        if (isBlank(sequence)) {
            return false;
        }
        int occurs = 0;
        char[] chars = sequence.toString().toCharArray();
        for (char ch : chars) {
            if (isDigit(ch)) {
                occurs++;
            }
        }

        return occurs < count;
    }

    /**
     * @param sequence target sequence
     * @param count    count of digit characters
     * @return occurs == count
     */
    public static boolean hasAtExactlySpecifiedCharacters(CharSequence sequence, int count) {
        if (isBlank(sequence)) {
            return false;
        }
        int occurs = 0;
        char[] chars = sequence.toString().toCharArray();
        for (char ch : chars) {
            if (isDigit(ch)) {
                occurs++;
            }
        }

        return occurs == count;
    }

    /**
     * @param sequence target sequence
     * @param count    count of specified characters
     * @return occurs >= count
     */
    public static boolean hasAtLeastLowerCaseCharacters(CharSequence sequence, int count) {
        if (isBlank(sequence)) {
            return false;
        }
        int occurs = 0;
        char[] chars = sequence.toString().toCharArray();
        for (char ch : chars) {
            if (isLowerCase(ch)) {
                occurs++;
            }
        }

        return occurs >= count;
    }

    /**
     * @param sequence target sequence
     * @param count    count of specified characters
     * @return occurs >= count
     */
    public static boolean hasAtLeastUpperCaseCharacters(CharSequence sequence, int count) {
        if (isBlank(sequence)) {
            return false;
        }
        int occurs = 0;
        char[] chars = sequence.toString().toCharArray();
        for (char ch : chars) {
            if (isUpperCase(ch)) {
                occurs++;
            }
        }

        return occurs >= count;
    }

    public static boolean hasOneOfCharacters(CharSequence sequence, CharSequence targetCharacters) {
        if (isBlank(sequence)) {
            return false;
        }

        String givenString = sequence.toString();
        char[] targetChars = targetCharacters.toString().toCharArray();
        for (char ch : targetChars) {
            if (givenString.contains(Character.toString(ch))) {
                return true;
            }
        }
        return false;
    }

    public static Set<String> generateSetOfCombinationWithPrefixWordAndCollection(CharSequence prefixWord,
                                                                                  Collection<? extends CharSequence> collection,
                                                                                  CharSequence separator) {
        return collection.stream()
                .map(element -> String.join(separator, prefixWord, element))
                .collect(Collectors.toSet());
    }

    public static Set<String> generateSetOfCombinationWithPrefixWordAndCollectionEnum(CharSequence prefixWord,
                                                                                      Collection<? extends Enum> collection,
                                                                                      CharSequence separator) {
        Collection<CharSequence> enumNames = collection.stream().map(Enum::name).collect(Collectors.toSet());
        return generateSetOfCombinationWithPrefixWordAndCollection(prefixWord, enumNames, separator);
    }


}

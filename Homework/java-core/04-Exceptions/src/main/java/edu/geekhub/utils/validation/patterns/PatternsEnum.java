package edu.geekhub.utils.validation.patterns;

public enum PatternsEnum {
    SPECIAL_CHARACTERS("[(){}`//\\]\\['”]+"),
    SPACES("[\\s]+"),
    WORDS("([\\w]+)"),
    ONE_WORD("^([\\w]+)$"),
    LOWERCASE("^([a-z0-9]+)$"),
    TWO_WORDS("^([\\w]+)\\s([\\w]+)$"),
    ONLY_LETTERS("[a-zA-Z]+"),
    CAMEL_CASE("^([A-Z][a-z0-9]+)\\s([A-Z][a-z0-9]+)$");

    private String pattern;

    PatternsEnum(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}

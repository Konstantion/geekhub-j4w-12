package edu.geekhub.utils.validation.patterns;

public enum PatternsEnum {
    SPECIAL_CHARACTERS("[(){}`//\\]\\['”]+"),
    ONE_WORD("^([\\S]+)$"),
    LOWERCASE("^([a-z0-9]+)$"),
    TWO_WORDS_SEPARATED_BY_SPACE("^([\\w]+)\\s([\\w]+)$"),
    ONLY_NOT_LETTERS("([^a-zA-Z\\s])"),
    CAMEL_CASE("(([A-Z][a-z0-9]+)\\s([A-Z][a-z0-9]+))+"),
    EMAIL("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    private String pattern;

    PatternsEnum(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}

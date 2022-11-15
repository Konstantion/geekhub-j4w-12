package edu.geekhub.utils.validation.messages;

public enum ValidationParameter {
    SPECIFIC_CHARACTERS("(){}`/\\]['”"),
    USER("User"),
    USER_ID("User ID"),
    USER_EMAIL("User email"),
    USERNAME("Username"),
    USER_FULL_NAME("User full name"),
    USER_AGE("User age"),
    MAX_AGE("100"),
    MIN_AGE("18"),
    USER_NOTES("User notes"),
    NOTES_LENGTH("255"),
    AMOUNT_OF_USER_FOLLOWERS("Amount of user followers"),

    SUCCESS_VALIDATION("Super secret hidden string that no one could even thing about :)");

    private String parameter;

    ValidationParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

}

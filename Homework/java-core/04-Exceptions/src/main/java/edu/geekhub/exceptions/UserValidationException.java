package edu.geekhub.exceptions;

import java.util.Objects;

public class UserValidationException extends Exception {

    private transient Object data;

    public UserValidationException(String message) {
        super(message);
    }

    public UserValidationException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public Object getData() {
        return Objects.isNull(data) ? "NULL" : data;
    }

}

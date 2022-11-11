package edu.geekhub.exceptions;

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
        return data;
    }

}

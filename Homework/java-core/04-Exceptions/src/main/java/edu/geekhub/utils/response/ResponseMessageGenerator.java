package edu.geekhub.utils.response;

import edu.geekhub.models.User;

import static edu.geekhub.utils.string.MessagesColors.*;

public class ResponseMessageGenerator {

    public static String successResponseMessage(User user) {
        return String.format(
                ANSI_GREEN
                        + "User have been successfully saved: "
                        + ANSI_RESET
                        + "[%s]",
                user.toString()
        );
    }

    public static String failedResponseMessage(String exceptionMessage, Object data) {
        return String.format(
                ANSI_RED
                        + "User haven't been successfully saved, error occurs: "
                        + ANSI_RESET
                        + "[%s, %s]",
                data.toString(),
                exceptionMessage
        );
    }

    private ResponseMessageGenerator() {

    }
}

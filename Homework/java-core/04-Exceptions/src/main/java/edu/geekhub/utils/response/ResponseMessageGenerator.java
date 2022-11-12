package edu.geekhub.utils.response;

import edu.geekhub.models.User;

import static edu.geekhub.utils.string.MessagesColors.*;

public class ResponseMessageGenerator {

    public static String successSaveUserResponseMessage(User user) {
        return String.format(
                ANSI_GREEN
                        + "User have been successfully saved: "
                        + ANSI_RESET
                        + "[%s]",
                user.toString()
        );
    }

    public static String failedValidationResponseMessage(String exceptionMessage, Object data) {
        return String.format(
                ANSI_RED
                        + "User haven't been successfully validated, error occurs: "
                        + ANSI_RESET
                        + "[%s : %s]",
                data.toString(),
                exceptionMessage
        );
    }
    public static String failedConnectToMemoryStorageResponseMessage(String exceptionMessage, Object data) {
        return String.format(
                ANSI_RED
                        + "Failed to connect to memory storage, error occurs: "
                        + ANSI_RESET
                        + "[%s : %s]",
                data.toString(),
                exceptionMessage
        );
    }
    public static String failedToCastDataToUser(String exceptionMessage, Object data) {
        return String.format(
                ANSI_RED
                        + "Failed to cast given data to User object: "
                        + ANSI_RESET
                        + "[%s : %s]",
                data.toString(),
                exceptionMessage
        );
    }

    private ResponseMessageGenerator() {

    }
}

package com.konstantion.email;

public interface EmailService {
    void send(String to, String email);

    String buildRegistrationEmail(String userName, String link);

    String buildRestorePasswordEmail(String password);

    String buildConfirmRestoreEmail(String link);
}

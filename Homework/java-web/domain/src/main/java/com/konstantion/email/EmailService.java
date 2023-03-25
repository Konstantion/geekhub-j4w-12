package com.konstantion.email;

public interface EmailService {
    void send(String to, String email);

    String buildRegistrationEmail(String userName, String link);
}

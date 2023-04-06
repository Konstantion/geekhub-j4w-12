package com.konstantion.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailUtil emailUtil;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    public EmailServiceImpl(JavaMailSender mailSender, EmailUtil emailUtil) {
        this.mailSender = mailSender;
        this.emailUtil = emailUtil;
    }

    @Async
    @Override
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("botunigroup@gmail.com");
            mailSender.send(mimeMessage);
            LOGGER.info("email successfully sent to {}", to);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
        }
    }

    @Override
    public String buildRegistrationEmail(String userName, String link) {
        return emailUtil.buildEmailTwo(userName, link);
    }


}

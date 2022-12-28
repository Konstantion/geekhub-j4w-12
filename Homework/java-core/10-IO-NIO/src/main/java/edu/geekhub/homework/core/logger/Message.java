package edu.geekhub.homework.core.logger;


import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static edu.geekhub.homework.core.logger.MessageType.ERROR;

public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private MessageType type;
    private String text;
    private final LocalDateTime date;
    private final String dateString;

    public Message(String text, MessageType type) {
        this.text = text;
        this.type = type;
        date = LocalDateTime.now();
        dateString = date.format(DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getDateString() {
        return dateString;
    }

    @Override
    public String toString() {
        return "Message{" +
               "type=" + type +
               ", text='" + text + '\'' +
               ", date=" + dateString +
               '}';
    }

    public String getLogString() {
        String color = type.equals(ERROR) ? "\u001B[31m" : "\u001B[32m";
        String reset = "\u001B[0m";
        return String.format(
                "%s|%S|%s|%s|%s%n",
                color,
                type,
                dateString,
                text,
                reset
        );
    }
}

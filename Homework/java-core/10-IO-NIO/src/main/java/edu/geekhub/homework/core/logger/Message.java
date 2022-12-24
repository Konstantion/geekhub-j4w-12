package edu.geekhub.homework.core.logger;


import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private MessageType type;
    private String text;
    private LocalDate date = LocalDate.now();

    public Message(String text, LocalDate date, MessageType type) {
        this.text = text;
        this.date = date;
        this.type = type;
    }

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
               "type=" + type +
               ", text='" + text + '\'' +
               ", date=" + date +
               '}';
    }
}

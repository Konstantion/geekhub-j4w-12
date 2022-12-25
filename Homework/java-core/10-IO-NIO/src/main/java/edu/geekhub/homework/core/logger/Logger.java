package edu.geekhub.homework.core.logger;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static final Logger instance = new Logger();
    private final LoggerIOUtil IOUtil;
    private final List<Message> messages;

    private Logger() {
        messages = new ArrayList<>();

        IOUtil = new LoggerIOUtil();
        IOUtil.readMessages(messages);
    }

    public static Logger getInstance() {
        return instance;
    }

    public void error(String text) {
        Message logMessage = new Message(text, MessageType.ERROR);
        messages.add(logMessage);
        save();
    }

    public void info(String text) {
        Message logMessage = new Message(text, MessageType.INFO);
        messages.add(logMessage);
        save();
    }

    private void save() {
        IOUtil.saveMessages(messages);
    }

    public List<Message> getMessages() {
        return messages;
    }
}

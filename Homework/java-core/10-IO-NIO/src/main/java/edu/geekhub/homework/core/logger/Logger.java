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
        IOUtil.fillMessagesListFromFile(messages);
    }

    public static Logger getInstance() {
        return instance;
    }

    public void error(String text) {
        Message logMessage = new Message(text, MessageType.ERROR);
        messages.add(logMessage);
        IOUtil.appendMessageToFile(logMessage);
    }

    public void info(String text) {
        Message logMessage = new Message(text, MessageType.INFO);
        messages.add(logMessage);
        IOUtil.appendMessageToFile(logMessage);
    }

    public List<Message> getMessages() {
        return messages;
    }
}

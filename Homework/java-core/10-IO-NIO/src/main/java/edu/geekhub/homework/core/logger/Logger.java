package edu.geekhub.homework.core.logger;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class Logger {
    private static Logger instance = null;
    private LoggerIOUtil IOUtil;
    private final List<Message> messages;

    private Logger() {
        messages = new ArrayList<>();

        IOUtil = new LoggerIOUtil();
        IOUtil.fillMessagesListFromFile(messages);
    }

    public void setIOUtil(LoggerIOUtil IOUtil) {
        this.IOUtil = IOUtil;
    }

    public static Logger getInstance() {
        if (isNull(instance)) {
            instance = new Logger();
        }
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

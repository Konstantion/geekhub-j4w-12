package edu.geekhub.homework.core.logger;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private LoggerIOUtil IOUtil;
    private final List<Message> messages;

    public Logger(LoggerIOUtil IOUtil) {
        this.IOUtil = IOUtil;
        messages = new ArrayList<>();
        IOUtil.fillMessagesListFromFile(messages);
    }

    public void setIOUtil(LoggerIOUtil IOUtil) {
        this.IOUtil = IOUtil;
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

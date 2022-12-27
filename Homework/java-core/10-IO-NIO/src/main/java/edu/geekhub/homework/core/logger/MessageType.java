package edu.geekhub.homework.core.logger;

public enum MessageType {
    ERROR(1),
    INFO(2);
    public final int val;
    MessageType(int val) {
        this.val = val;
    }
}

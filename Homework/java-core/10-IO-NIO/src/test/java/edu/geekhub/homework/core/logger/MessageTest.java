package edu.geekhub.homework.core.logger;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MessageTest {
    @Test
    void processShouldInitializeMessage_whenCreateMessage() {
        Message message = new Message("text", MessageType.INFO);

        assertThat(message).hasNoNullFieldsOrProperties();
    }

    @Test
    void processShouldReturnLogString_whenGetLogString() {
        Message message = new Message("text", MessageType.ERROR);

        assertThat(message.getLogString()).contains("\u001B[0m");
    }

    @Test
    void processShouldReturnText_whenSetText() {
        Message message = new Message("text", MessageType.ERROR);
        message.setText("new Text");

        assertThat(message.getLogString()).contains("new Text");
    }

    @Test
    void processShouldReturnType_whenSetType() {
        Message message = new Message("text", MessageType.ERROR);
        message.setType(MessageType.INFO);

        assertThat(message.getType()).isEqualTo(MessageType.INFO);
    }

    @Test
    void processShouldReturnString_whenToString() {
        Message message = new Message("text", MessageType.ERROR);


        assertThat(message.toString()).contains("Message{");
    }
}
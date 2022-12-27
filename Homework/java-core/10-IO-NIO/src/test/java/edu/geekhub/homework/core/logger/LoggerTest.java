package edu.geekhub.homework.core.logger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class LoggerTest {

    @Mock
    private LoggerIOUtil IOUtil;

    @Test
    void process_returnSameLogger_whenGetInstance() {
        Logger loggerFirst = Logger.getInstance();

        Logger loggerSecond = Logger.getInstance();

        assertThat(loggerFirst).isEqualTo(loggerSecond);
    }

    @Test
    void process_returnFalse_whenAnotherObject() {
        Logger loggerFirst = Logger.getInstance();

        String string = "How am i";

        assertThat(loggerFirst).isNotEqualTo(string);
    }

    @Test
    void process_addMessageWithTagInfo_whenLongInfo() {
        Logger logger = spy(Logger.class);
        logger.setIOUtil(IOUtil);

        doNothing().when(IOUtil).appendMessageToFile(any(Message.class));
        logger.info("Test log");
        List<Message> messages = logger.getMessages();
        assertThat(messages.get(messages.size() - 1))
                .extracting(Message::getText)
                .isEqualTo("Test log");

        assertThat(messages.get(messages.size() - 1))
                .extracting(Message::getType)
                .isEqualTo(MessageType.INFO);
    }

    @Test
    void process_addMessageWithTagError_whenLongError() {
        Logger logger = spy(Logger.class);
        logger.setIOUtil(IOUtil);

        doNothing().when(IOUtil).appendMessageToFile(any(Message.class));
        logger.error("Test log");
        List<Message> messages = logger.getMessages();
        assertThat(messages.get(messages.size() - 1))
                .extracting(Message::getText)
                .isEqualTo("Test log");

        assertThat(messages.get(messages.size() - 1))
                .extracting(Message::getType)
                .isEqualTo(MessageType.ERROR);
    }

    @Test
    void process_returnHash_whenLoggerHashCode() {
        assertThat(Logger.getInstance().hashCode()).isNotZero();
    }
}
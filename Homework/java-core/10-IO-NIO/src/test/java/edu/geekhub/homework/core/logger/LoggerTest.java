package edu.geekhub.homework.core.logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggerTest {
    private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private static final String SEPARATOR = System.getProperty("file.separator");

    private Logger logger;

    @Spy
    private LoggerIOUtil IOUtil;

    @BeforeEach
    void setUp() {
        String returnedPath = String.join(SEPARATOR,
                PROJECT_DIRECTORY,
                "Homework",
                "java-core",
                "10-IO-NIO",
                "src",
                "main",
                "resources"
        );
        String pathDiff = String.join(SEPARATOR.repeat(2),
                "",
                "Homework",
                "java-core",
                "10-IO-NIO");
        String currentPath = returnedPath.replaceFirst(pathDiff, "");
        when(IOUtil.buildResourcesPath())
                .thenReturn(currentPath);
        logger = new Logger(IOUtil);
        logger.setIOUtil(IOUtil);
    }

    @Test
    void process_returnFalse_whenAnotherObject() {
        String string = "How am i";

        assertThat(logger).isNotEqualTo(string);
    }

    @Test
    void process_addMessageWithTagInfo_whenLongInfo() {
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
        assertThat(logger.hashCode()).isNotZero();
    }
}
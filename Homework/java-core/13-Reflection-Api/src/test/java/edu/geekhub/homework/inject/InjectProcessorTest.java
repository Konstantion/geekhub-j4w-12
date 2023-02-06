package edu.geekhub.homework.inject;

import edu.geekhub.homework.TestClass;
import edu.geekhub.homework.exceptions.PropertiesNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

class InjectProcessorTest {
    private static final String TEST_PROPERTIES = "test.properties";
    private static final String TEST_STRING = "test=test";

    @Test
    void process_shouldReturnPropertiesLines_whenGetPropertiesLines() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getPropertiesLines = InjectProcessor.class
                .getDeclaredMethod("getPropertiesLines", String.class);
        getPropertiesLines.setAccessible(true);
        List<String> actualLines = (List<String>) getPropertiesLines
                .invoke(null, TEST_PROPERTIES);

        assertThat(actualLines)
                .isNotEmpty()
                .contains(TEST_STRING);
    }

    @Test
    void process_shouldThrowException_whenGetPropertiesLines_fileNameNotExist() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getPropertiesLines = InjectProcessor.class
                .getDeclaredMethod("getPropertiesLines", String.class);
        getPropertiesLines.setAccessible(true);
        assertThatThrownBy(() -> getPropertiesLines
                .invoke(null, "Not existing file"))
                .cause()
                .isInstanceOf(PropertiesNotFoundException.class);

    }

    @Test
    void process_shouldThrowException_whenGetPropertiesLines_readAllLinesException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try(MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllLines(any(Path.class))).thenThrow(IOException.class);
            Method getPropertiesLines = InjectProcessor.class
                    .getDeclaredMethod("getPropertiesLines", String.class);
            getPropertiesLines.setAccessible(true);
            assertThatThrownBy(() -> getPropertiesLines
                    .invoke(null, TEST_PROPERTIES))
                    .cause()
                    .isInstanceOf(PropertiesNotFoundException.class);
        }
    }

    @Test
    void process_shouldReturnMapOfProperties_whenExtractPropertiesLinesToMap() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method extractPropertiesLinesToMap = InjectProcessor.class
                .getDeclaredMethod("extractPropertiesLinesToMap", List.class);
        extractPropertiesLinesToMap.setAccessible(true);

        List<String> actualLines = List.of("test=test", "test1=test1");

        Map<String, Object> actualProperties = (Map<String, Object>) extractPropertiesLinesToMap
                .invoke(null, actualLines);

        assertThat(actualProperties).contains(entry("test", "test"), entry("test1", "test1"));
    }

    @Test
    void process_shouldSetValueToField_whenSetValueToField() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        TestClass testClass = new TestClass();
        Field stringField = TestClass.class.getField("stringField");
        Field integerField = TestClass.class.getField("integerField");
        Field enumField = TestClass.class.getField("enumField");

        Method setValueToField = InjectProcessor.class
                .getDeclaredMethod("setValueToField", Object.class, Field.class, Object.class);
        setValueToField.setAccessible(true);
        setValueToField.invoke(null, testClass, stringField, "String");
        setValueToField.invoke(null, testClass, integerField, "123");
        setValueToField.invoke(null, testClass, enumField, Month.APRIL.toString());

        assertThat(testClass.stringField)
                .isEqualTo("String");
        assertThat(testClass.integerField)
                .isEqualTo(123);
        assertThat(testClass.enumField)
                .isEqualTo(Month.APRIL);
    }

    @Test
    void process_shouldSetPropertiesToObject_whenProcess() {
        InjectProcessor injectProcessor = new InjectProcessor(TEST_PROPERTIES);
        TestClass testClass = new TestClass();

        injectProcessor.process(testClass);

        assertThat(testClass.stringField).isEqualTo("test");
        assertThat(testClass.integerField).isEqualTo(123);
        assertThat(testClass.enumField).isNull();
    }

    @Test
    void process_shouldCreateInjectProcessorWithDefaultPath_whenConstructorWithoutParameter() {
        InjectProcessor injectProcessor = new InjectProcessor();

        assertThat(injectProcessor.getPropertiesFile())
                .isEqualTo("application.properties");
    }

    @Test
    void process_shouldInformUser_whenPropertiesNotFound() {
        InjectProcessor injectProcessor = new InjectProcessor("NOT EXIST");
    }
}
package edu.geekhub.homework.util;

import edu.geekhub.homework.TestClass;
import edu.geekhub.homework.exceptions.PropertiesFormatException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReflectionUtilsTest {
    @Test
    void process_shouldReturnTrue_whenIsFieldOfTypeTypesAreTheSame() throws NoSuchFieldException {
        Field stringField = TestClass.class.getField("stringField");

        boolean actualResult = ReflectionUtils.isFieldOfType(stringField, stringField.getType());

        assertThat(actualResult).isTrue();
    }

    @Test
    void process_shouldReturnFalse_whenIsFieldOfTypeTypesAreDifferent() throws NoSuchFieldException {
        Field stringField = TestClass.class.getField("stringField");

        boolean actualResult = ReflectionUtils.isFieldOfType(stringField, Integer.class);

        assertThat(actualResult).isFalse();
    }

    @Test
    void process_shouldReturnTrue_whenIsFieldIntegerOrInt_FieldIsIntegerOrInt() throws NoSuchFieldException {
        Field integerField = TestClass.class.getField("integerField");
        Field intField = TestClass.class.getField("intField");

        boolean actualResult = ReflectionUtils.isFieldIntegerOrInt(integerField)
                               && ReflectionUtils.isFieldIntegerOrInt(intField);

        assertThat(actualResult).isTrue();
    }

    @Test
    void process_shouldReturnFalse_whenIsFieldIntegerOrInt_FieldIsNotIntegerOrInt() throws NoSuchFieldException {
        Field stringField = TestClass.class.getField("stringField");

        boolean actualResult = ReflectionUtils.isFieldIntegerOrInt(stringField);

        assertThat(actualResult).isFalse();
    }

    @Test
    void process_shouldReturnTrue_whenIsFieldEnum_FieldIsEnum() throws NoSuchFieldException {
        Field enumField = TestClass.class.getField("enumField");

        boolean actualResult = ReflectionUtils.isFieldEnum(enumField);

        assertThat(actualResult).isTrue();
    }

    @Test
    void process_shouldReturnFalse_whenIsFieldEnum_FieldIsNotEnum() throws NoSuchFieldException {
        Field stringField = TestClass.class.getField("stringField");

        boolean actualResult = ReflectionUtils.isFieldEnum(stringField);

        assertThat(actualResult).isFalse();
    }

    @Test
    void process_shouldReturnTrue_whenIsObjectCanBeParsedToInteger_ObjectCanBeParsed() throws PropertiesFormatException {
        Object obj = "123";

        boolean actualResult = ReflectionUtils.isObjectCanBeParsedToInteger(obj);

        assertThat(actualResult).isTrue();
    }

    @Test
    void process_throwException_whenIsObjectCanBeParsedToInteger_ObjectCanNotBeParsed() {
        Object obj = "Not Integer";

        assertThatThrownBy(() -> ReflectionUtils.isObjectCanBeParsedToInteger(obj))
                .isInstanceOf(PropertiesFormatException.class);
    }

    @Test
    void process_shouldReturnEnumInstance_whenTryToParseObjectToEnum_ObjectCanBeParsed() throws PropertiesFormatException {
        Object obj = Month.APRIL.toString();

        Month actualMonth = ReflectionUtils.tryToParseObjectToEnum(obj, Month.class);

        assertThat(actualMonth)
                .isNotNull()
                .isEqualTo(Month.APRIL);
    }

    @Test
    void process_throwException_whenTryToParseObjectToEnum_ObjectCanNotBeParsed() {
        Object obj = "Month.APRIL.toString()";

        assertThatThrownBy(() -> ReflectionUtils.tryToParseObjectToEnum(obj, Month.class))
                .isInstanceOf(PropertiesFormatException.class);
    }
}
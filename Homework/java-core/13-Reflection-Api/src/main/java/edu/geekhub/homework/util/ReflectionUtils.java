package edu.geekhub.homework.util;

import edu.geekhub.homework.exceptions.PropertiesFormatException;

import java.lang.reflect.Field;

import static java.lang.String.format;

public class ReflectionUtils {
    private ReflectionUtils() {

    }

    public static boolean isFieldOfType(Field field, Class<?> type) {
        return field.getType().equals(type);
    }

    public static boolean isFieldIntegerOrInt(Field field) {
        return isFieldOfType(field, Integer.class) || isFieldOfType(field, int.class);
    }

    public static boolean isFieldEnum(Field field) {
        return field.getType().isEnum();
    }

    public static boolean isObjectCanBeParsedToInteger(Object obj) throws PropertiesFormatException {
        try {
            Integer.parseInt(obj.toString());
            return true;
        } catch (NumberFormatException e) {
            throw new PropertiesFormatException(
                    format("Can't convert value %s, to the Integer", obj)
            );
        }
    }

    public static <T extends Enum<T>> T tryToParseObjectToEnum(Object obj, Class<T> enumClass) throws PropertiesFormatException {
        try {
            return Enum.valueOf(enumClass, obj.toString());
        } catch (Exception e) {
            throw new PropertiesFormatException(
                    format("Can't convert value %s, to %s, %s",
                            obj,
                            enumClass.getName(),
                            e.getMessage()
                    )
            );
        }
    }
}

package edu.geekhub.homework.inject;

import edu.geekhub.homework.ApplicationStarter;
import edu.geekhub.homework.exceptions.PropertiesFormatException;
import edu.geekhub.homework.exceptions.PropertiesNotFoundException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static edu.geekhub.homework.util.ReflectionUtils.*;
import static java.lang.String.format;
import static java.util.Objects.isNull;

public class InjectProcessor {
    private final Map<String, Object> properties;
    private static final String PREFIX = "gh.inject.";
    private static final Logger LOGGER = Logger.getLogger(InjectProcessor.class.getName());

    public InjectProcessor() {
        List<String> propLines = getPropertiesLines();
        properties = extractPropertiesLinesToMap(propLines);
    }

    public void process(Object target) {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Injectable.class)) {
                break;
            }
            final String propName;

            Injectable annotation = field.getAnnotation(Injectable.class);

            propName = annotation.propertyName().isBlank() ?
                    field.getName() : annotation.propertyName();

            if (properties.containsKey(propName)) {
                try {
                    field.setAccessible(true);
                    setValueToField(target, field, properties.get(propName));

                } catch (IllegalAccessException | PropertiesFormatException e) {
                    LOGGER.warning(format(
                            "Can't set value %s to field %s, because %s",
                            properties.get(propName),
                            field.getName(),
                            e.getMessage())
                    );
                } finally {
                    field.setAccessible(false);
                }
            }
        }
    }

    private boolean setValueToField(Object target, Field field, Object value) throws IllegalAccessException, PropertiesFormatException {
        if (isFieldOfType(field, String.class)) {
            field.set(target, value);
            return true;
        }

        if (isFieldIntegerOrInt(field)) {
            isObjectCanBeParsedToInteger(value);
            field.set(target, Integer.valueOf(value.toString()));
            return true;
        }

        if (isFieldEnum(field)) {
            Enum parsedEnum = tryToParseObjectToEnum(value, (Class<? extends Enum>) field.getType());
            field.set(target, parsedEnum);
            return true;
        }

        return false;
    }

    private List<String> getPropertiesLines() {
        URL url = ApplicationStarter.class.getClassLoader().getResource("application.properties");
        if (isNull(url)) {
            throw new PropertiesNotFoundException("File application.properties wasn't found");
        }
        try {
            return Files.readAllLines(Path.of(url.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new PropertiesNotFoundException(e.getMessage());
        }
    }

    private Map<String, Object> extractPropertiesLinesToMap(List<String> propLines) {
        return propLines
                .stream()
                .map(prop -> {
                    boolean containsPrefix = prop.contains(PREFIX);
                    String result = prop;
                    if (containsPrefix) {
                        result = prop.substring(PREFIX.length());
                    }
                    return result;
                })
                .map(prop -> prop.trim().split("=", 2))
                .collect(Collectors.toMap(
                        pair -> pair[0],
                        pair -> pair[1]
                ));
    }
}

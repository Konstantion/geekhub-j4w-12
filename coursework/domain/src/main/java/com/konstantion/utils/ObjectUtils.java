package com.konstantion.utils;

import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class ObjectUtils {
    private ObjectUtils() {
    }

    public static <T, U> boolean anyMatchCollection(Collection<T> entities, Function<T, U> extractor, U desired, BiPredicate<U, U> criteria) {
        return entities.stream()
                .map(extractor)
                .anyMatch(value -> criteria.test(desired, value));
    }

    public static <T> T requireNonNullOrElseNullable(T object, @Nullable T nullable) {
        return (object != null) ? object : nullable;
    }
}

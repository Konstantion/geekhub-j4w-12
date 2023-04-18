package com.konstantion.utils;

import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Function;

public record ObjectUtils() {
    public static <T, U> boolean anyMatchCollection(Collection<T> entities, Function<T, U> extractor, U desired, BiPredicate<U, U> criteria) {
        return entities.stream()
                .map(extractor)
                .anyMatch(value -> criteria.test(desired, value));
    }
}

package com.konstantion.utils;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

public class HashMaps {
    private HashMaps() {

    }

    public static <K, V> Map<K, V> of(@NonNull K key, @Nullable V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}

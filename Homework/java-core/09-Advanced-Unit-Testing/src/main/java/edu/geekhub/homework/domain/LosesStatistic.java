package edu.geekhub.homework.domain;

import edu.geekhub.homework.client.LosesStatisticHttpClient;

import java.util.Map;
import java.util.Objects;


/**
 * This class should contain data received through {@link LosesStatisticHttpClient}
 * via <a href="https://en.wikipedia.org/wiki/JSON">JSON</a> String
 */
public record LosesStatistic(Map<String, Integer> data) {
    private static final String ID = "id";

    public LosesStatistic {
        Objects.requireNonNull(data);

        if (!data.containsKey(ID)) {
            throw new IllegalArgumentException("Object data must contain key \"ID\"");
        }

    }

    public int id() {
        return data.get(ID);
    }

    @Override
    public String toString() {
        return data.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof  LosesStatistic that
                && that.data.equals(data);
    }
}

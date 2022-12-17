package edu.geekhub.homework.domain;

import edu.geekhub.homework.client.LosesStatisticHttpClient;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;


/**
 * This class should contain data received through {@link LosesStatisticHttpClient}
 * via <a href="https://en.wikipedia.org/wiki/JSON">JSON</a> String
 */
public record LosesStatistic(Map<String, Integer> data) {
    private static final String ID = "id";

    public static final LosesStatistic EMPTY_STATISTIC = new LosesStatistic(
            Collections.singletonMap(ID, Integer.MIN_VALUE)
    );

    public LosesStatistic {
        Objects.requireNonNull(data);

        if (!data.containsKey(ID)) {
            throw new IllegalArgumentException("Object data must contain key \"id\"");
        }
    }

    public int id() {
        return data.get(ID);
    }

    public Map<String, Integer> data() {
        if (this.equals(EMPTY_STATISTIC)) {
            return Collections.emptyMap();
        }

        return data;
    }

    @Override
    public String toString() {
        return data.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LosesStatistic that
               && that.data.equals(data);
    }
}

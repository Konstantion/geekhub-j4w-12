package edu.geekhub.homework.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class LosesStatisticTest {

    @Test
    void process_entityShouldContainId_whenWePassDataWithId() {
        Map<String, Integer> data = new HashMap<>();
        String key = "id";
        int value = 10;
        data.put(key, value);
        data.put("tank", 8);

        LosesStatistic losesStatistic = new LosesStatistic(data);

        assertThat(losesStatistic.id())
                .isEqualTo(value);
    }

    @Test
    void process_entityShouldContainData_whenWePassData() {
        Map<String, Integer> data = new HashMap<>();
        String key = "id";
        int value = 10;
        data.put(key, value);
        data.put("tank", 8);

        LosesStatistic losesStatistic = new LosesStatistic(data);

        assertThat(losesStatistic)
                .extracting(LosesStatistic::data)
                .isEqualTo(data);
    }

    @Test
    void process_shouldThrowsArgumentException_whenWePassDataWithoutId() {
        Map<String, Integer> data = new HashMap<>();
        String key = "tank";
        int value = 10;
        data.put(key, value);
        data.put("tank", 8);

        assertThatCode(
                () -> new LosesStatistic(data)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void process_shouldThrowsNullPointerException_whenWePassDataNull() {
        Map<String, Integer> data = null;


        assertThatCode(
                () -> new LosesStatistic(data)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void process_shouldReturnTrue_whenWeEqualSameObjects() {
        Map<String, Integer> data = new HashMap<>();
        String key = "id";
        int value = 10;
        data.put(key, value);
        data.put("tank", 8);

        LosesStatistic losesStatistic = new LosesStatistic(data);
        LosesStatistic losesStatistic1 = new LosesStatistic(data);

        assertThat(losesStatistic)
                .isEqualTo(losesStatistic1);
    }

    @Test
    void process_shouldReturnFalse_whenWeEqualDifferentObjects() {
        Map<String, Integer> data = new HashMap<>();
        Map<String, Integer> data1 = new HashMap<>();
        String key = "id";
        int value = 10;
        data.put(key, value);
        data.put("tank", 8);
        data1.put("id", 20);

        LosesStatistic losesStatistic = new LosesStatistic(data);
        LosesStatistic losesStatistic1 = new LosesStatistic(data1);

        assertThat(losesStatistic)
                .isNotEqualTo(losesStatistic1);
    }

    @Test
    void process_entityShouldReturnString_whenWeToString() {
        Map<String, Integer> data = new HashMap<>();
        String key = "id";
        int value = 10;
        data.put(key, value);
        data.put("tank", 8);

        LosesStatistic losesStatistic = new LosesStatistic(data);

        assertThat(losesStatistic)
                .hasToString(data.toString());
    }

    @Test
    void process_shouldBeEmptyData_whenEntityEMPTY() {
        LosesStatistic statistic = LosesStatistic.EMPTY_STATISTIC;

        assertThat(statistic)
                .returns(Integer.MIN_VALUE, LosesStatistic::id)
                .extracting(LosesStatistic::data)
                .isEqualTo(Collections.emptyMap());

    }

    @Test
    void process_shouldReturnInteger_whenLosesAmount() {
        Map<String, Integer> data = new HashMap<>();
        int value = 10;
        data.put("id", 1);
        data.put("tank", value);
        data.put("car", value);
        data.put("orc", value);

        LosesStatistic losesStatistic = new LosesStatistic(data);

        assertThat(losesStatistic)
                .extracting(LosesStatistic::losesAmount)
                .isEqualTo(value * 3);
    }

    @Test
    void process_shouldReturnZero_whenLosesEmpty() {
        LosesStatistic losesStatistic = LosesStatistic.EMPTY_STATISTIC;

        assertThat(losesStatistic)
                .extracting(LosesStatistic::losesAmount)
                .isEqualTo(0);
    }
}
package edu.geekhub.homework.client;

import edu.geekhub.homework.domain.LosesStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class JsonConverterTest {

    private JsonConverter converter;

    @BeforeEach
    void setUp() {
        converter = new JsonConverter();
    }

    @Test
    void process_shouldReturnEntry_whenWeParseJSON() {
        String json = "{" +
                      "\"age\":\"20\"," +
                      "\"id\":\"1\"" +
                      "}";
        LosesStatistic actualStatistic = converter.convertToEntity(json);

        assertThat(actualStatistic.data())
                .containsOnly(entry("age", 20), entry("id", 1));
    }

    @Test
    void process_shouldReturnEntries_whenWeParseListOfObjectsJSON() {
        String json = "[{" +
                      "\"age\":\"20\"," +
                      "\"id\":\"1\"" +
                      "},{" +
                      "\"age2\":\"25\"," +
                      "\"id2\":\"2\"" +
                      "}]";
        List<LosesStatistic> actualStatistics = converter.convertToEntities(json);

        assertThat(actualStatistics
                .stream()
                .map(s -> s.data().entrySet())
                .reduce(
                        new HashSet<>(),
                        (acc, curr) -> Stream.concat(
                                acc.stream(),
                                curr.stream()
                        ).collect(Collectors.toSet()))
        ).containsOnly(
                entry("age", 20),
                entry("id", 1),
                entry("age2", 25),
                entry("id2", 2));
    }

    @Test
    void process_shouldReturnJson_whenWeParseEntry() {
        String expectedJson = "{" +
                              "\"age\":\"20\"," +
                              "\"id\":\"1\"" +
                              "}";

        LosesStatistic statistic = converter.convertToEntity(expectedJson);

        String actualJson = converter.convertToJson(statistic);

        assertThat(actualJson).isEqualTo(expectedJson);
    }

}
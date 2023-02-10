package edu.geekhub.homework.client;

import edu.geekhub.homework.domain.LosesStatistic;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class is responsible for conversion of {@link String} objects into a
 * {@link LosesStatistic} objects and otherwise
 */
public class JsonConverter {

    public List<LosesStatistic> convertToEntities(String losesStatisticsJson) {
        return Arrays.stream(losesStatisticsJson
                        .replaceAll("[\\[|\\]\\s]", "")
                        .split("},\\{"))
                .filter(Predicate.not(String::isBlank))
                .map(this::convertToEntity)
                .toList();
    }

    public LosesStatistic convertToEntity(String losesStatisticJson) {
        Map<String, Integer> data = Arrays.stream(losesStatisticJson
                        .replaceAll("[{}\"\\s]", "")
                        .replace(":", " ")
                        .split(","))
                .collect(Collectors.toMap(
                        s -> s.split(" ")[0],
                        s -> Integer.valueOf(s.split(" ")[1]),
                        Integer::sum,
                        LinkedHashMap::new
                ));

        return new LosesStatistic(data);
    }

    public String convertToJson(LosesStatistic losesStatistic) {
        Map<String, Integer> data = losesStatistic.data();

        StringBuilder jsonBuilder = data.entrySet()
                .stream()
                .map(entry -> String.format(
                        "\"%s\":\"%s\",",
                        entry.getKey(),
                        entry.getValue())
                )
                .reduce(new StringBuilder(),
                        StringBuilder::append,
                        StringBuilder::append)
                .insert(0, "{")
                .append("}");

        jsonBuilder.deleteCharAt(
                jsonBuilder.lastIndexOf(",")
        );

        return jsonBuilder.toString();
    }


}

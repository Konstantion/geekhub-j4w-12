package edu.geekhub.homework.domain;

import edu.geekhub.homework.client.JsonConverter;
import edu.geekhub.homework.client.LosesStatisticHttpClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Service should fetch loses statistic data as a {@link String} object, then convert it into a
 * {@link LosesStatistic} by using {@link } class and return a result if possible.
 * <br/>
 * <br/>
 * IMPORTANT: DO NOT CHANGE METHODS SIGNATURE
 */
public class LosesStatisticService {

    private final JsonConverter converter;
    private final LosesStatisticHttpClient httpClient;
    public static final String SERVER_ERROR_RESPONSE = "Something went wrong while parsing response JSON";

    public LosesStatisticService(JsonConverter converter, LosesStatisticHttpClient httpClient) {
        this.converter = converter;
        this.httpClient = httpClient;
    }

    public List<LosesStatistic> getAll() {
        List<LosesStatistic> statistics;
        try {
            String allEntitiesJson = httpClient.getAll();

            if (allEntitiesJson.equals(SERVER_ERROR_RESPONSE)) {
                statistics = Collections.emptyList();
            } else {
                statistics = converter.convertToEntities(allEntitiesJson);
            }
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();

            statistics = Collections.emptyList();
        }
        return statistics;
    }

    public LosesStatistic getById(Integer id) {
        LosesStatistic statistic;
        try {
            String entityJson = httpClient.getById(id);

            if (entityJson.equals(SERVER_ERROR_RESPONSE)) {
                statistic = LosesStatistic.EMPTY_STATISTIC;
            } else {
                statistic = converter.convertToEntity(entityJson);
            }
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();

            statistic = LosesStatistic.EMPTY_STATISTIC;
        }
        return statistic;
    }

    public void deleteAll() {
        try {
            httpClient.deleteAll();
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void deleteById(int id) {
        try {
            httpClient.deleteById(id);
        } catch (InterruptedException | IOException e) {
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void create(LosesStatistic losesStatistic) {
        try {
            if (!losesStatistic.data().equals(Collections.emptyMap())) {
                String json = converter.convertToJson(losesStatistic);
                httpClient.create(json);
            }
        } catch (InterruptedException | IOException e) {

            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}

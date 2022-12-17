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

    public LosesStatisticService(JsonConverter converter, LosesStatisticHttpClient httpClient) {
        this.converter = converter;
        this.httpClient = httpClient;
    }

    public List<LosesStatistic> getAll() {
        try {
            String allEntitiesJson = httpClient.getAll();

            return converter.convertToEntities(allEntitiesJson);
        } catch (InterruptedException | IOException e) {

            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();

            return Collections.emptyList();
        }
    }

    public LosesStatistic getById(Integer id) {
        try {
            String entityJson = httpClient.getById(id);

            return converter.convertToEntity(entityJson);
        } catch (InterruptedException | IOException e) {

            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();

            return LosesStatistic.EMPTY_STATISTIC;
        }
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
            String json = converter.convertToJson(losesStatistic);
            httpClient.create(json);
        } catch (InterruptedException | IOException e) {

            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}

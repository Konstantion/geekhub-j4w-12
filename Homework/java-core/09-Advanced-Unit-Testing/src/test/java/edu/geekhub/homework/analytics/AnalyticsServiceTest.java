package edu.geekhub.homework.analytics;

import edu.geekhub.homework.client.JsonConverter;
import edu.geekhub.homework.domain.LosesStatistic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AnalyticsServiceTest {

    JsonConverter converter;

    AnalyticsService analyticsService;

    final String entitiesJson = "[" +
                                "{\"tanks\":\"2\"," +
                                "\"armouredFightingVehicles\":\"2\"," +
                                "\"id\": \"8\"}" +
                                "," +
                                " {\"tanks\":\"1\"," +
                                "\"armouredFightingVehicles\":\"6\"," +
                                "\"id\":\"9\"}" +
                                "]";
    final String entityJson = "{\"tanks\":\"4\"," +
                              "\"armouredFightingVehicles\":\"2\"," +
                              "\"id\":\"8\"}";

    @BeforeEach
    void setUp() {
        analyticsService = new AnalyticsService();
        converter = new JsonConverter();
    }

    @Test
    void process_shouldReturnAmountOfLoses_whenTotalCountOfLosesAmount() {
        List<LosesStatistic> losesStatistics = converter.convertToEntities(entitiesJson);

        int expectedAmount = 11;
        int actualAmount = analyticsService.totalCountOfLosesForAllStatistics(losesStatistics);

        assertThat(actualAmount).isEqualTo(expectedAmount);
    }

    @Test
    void process_shouldReturnMaxLoses_whenStatisticWithMaxLoses() {
        List<LosesStatistic> losesStatistics = converter.convertToEntities(entitiesJson);

        int expectedId = 9;
        int actualId = analyticsService
                .findStatisticWithMaxLosesAmounts(losesStatistics)
                .id();

        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    void process_shouldReturnMinLoses_whenStatisticWithMinLoses() {
        List<LosesStatistic> losesStatistics = converter.convertToEntities(entitiesJson);

        int expectedId = 8;
        int actualId = analyticsService
                .findStatisticWithMinLosesAmounts(losesStatistics)
                .id();

        assertThat(actualId).isEqualTo(expectedId);
    }

    @Test
    void process_shouldReturnCountOfLoses_whenStatisticCountOfLoss() {
        LosesStatistic losesStatistic = converter.convertToEntity(entityJson);

        int expectedCount = 6;
        int actualCount = analyticsService.totalCountOfLosesForStatistic(losesStatistic);

        assertThat(actualCount).isEqualTo(expectedCount);
    }
}
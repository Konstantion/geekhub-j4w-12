package edu.geekhub.homework.domain;

import edu.geekhub.homework.client.JsonConverter;
import edu.geekhub.homework.client.LosesStatisticHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LosesStatisticServiceTest {

    @Mock
    private LosesStatisticHttpClient httpClient;

    private LosesStatisticService service;

    private JsonConverter converter;

    private final String entitiesJson = "[" +
                                        "{\"tanks\":\"2\"," +
                                        "\"armouredFightingVehicles\":\"2\"," +
                                        "\"id\": \"8\"}" +
                                        "," +
                                        " {\"tanks\":\"1\"," +
                                        "\"armouredFightingVehicles\":\"6\"," +
                                        "\"id\":\"9\"}" +
                                        "]";
    private final String entityJson = "{\"tanks\":\"2\"," +
                                      "\"armouredFightingVehicles\":\"2\"," +
                                      "\"id\": \"8\"}";

    @BeforeEach
    void setUp() {
        converter = new JsonConverter();
        service = new LosesStatisticService(converter, httpClient);
    }

    @Test
    void process_shouldReturnAllStatistics_whenGetAllNotInterrupted() throws IOException, InterruptedException {
        when(httpClient.getAll()).thenReturn(entitiesJson);

        List<LosesStatistic> actualStatistics = service.getAll();
        List<LosesStatistic> expectedStatistics = converter.convertToEntities(entitiesJson);
        assertThat(actualStatistics).isEqualTo(expectedStatistics);
    }

    @Test
    void process_shouldReturnEmptyList_whenErrorOccurs() throws IOException, InterruptedException {
        when(httpClient.getAll()).thenThrow(InterruptedException.class);

        List<LosesStatistic> actualStatistics = service.getAll();
        List<LosesStatistic> expectedStatistics = Collections.emptyList();
        assertThat(actualStatistics).isEqualTo(expectedStatistics);
    }

    @Test
    void process_shouldReturnEntity_whenGetById() {

    }


}
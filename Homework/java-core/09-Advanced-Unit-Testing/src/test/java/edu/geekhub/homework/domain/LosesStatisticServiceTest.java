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
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.*;

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
                                      "\"id\":\"8\"}";

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
    void process_shouldReturnEmptyList_whenErrorOccursIntGetAll() throws IOException, InterruptedException {
        when(httpClient.getAll()).thenThrow(InterruptedException.class);

        List<LosesStatistic> actualStatistics = service.getAll();
        List<LosesStatistic> expectedStatistics = Collections.emptyList();
        assertThat(actualStatistics).isEqualTo(expectedStatistics);
    }

    @Test
    void process_shouldReturnEntity_whenGetById() throws IOException, InterruptedException {
        int ID = 10;
        when(httpClient.getById(ID)).thenReturn(entityJson);

        LosesStatistic actualStatistic = service.getById(ID);
        LosesStatistic expectedStatistic = converter.convertToEntity(entityJson);
        assertThat(actualStatistic).isEqualTo(expectedStatistic);
    }

    @Test
    void process_shouldReturnEmptyEntity_whenErrorOccursInGetById() throws IOException, InterruptedException {
        int ID = -10;
        when(httpClient.getById(intThat(i -> i < 0))).thenThrow(InterruptedException.class);

        LosesStatistic statistic = service.getById(ID);

        assertThat(statistic)
                .isEqualTo(LosesStatistic.EMPTY_STATISTIC);
    }

    @Test
    void process_shouldReturnEmptyEntity_whenServerReturnErrorMessage() throws IOException, InterruptedException {
        int ID = 0;
        when(httpClient.getById(ID)).thenReturn(LosesStatisticService.SERVER_ERROR_RESPONSE);

        LosesStatistic statistic = service.getById(ID);

        assertThat(statistic)
                .isEqualTo(LosesStatistic.EMPTY_STATISTIC);
    }

    @Test
    void process_shouldInvokeClientDeleteAll_whenServiceDeleteAll() throws IOException, InterruptedException {
        doNothing().when(httpClient).deleteAll();
        service.deleteAll();

        verify(httpClient).deleteAll();
    }

    @Test
    void process_shouldInvokeClientDeleteAll_whenErrorOccursInDeleteAll() throws IOException, InterruptedException {
        doThrow(InterruptedException.class).when(httpClient).deleteAll();

        service.deleteAll();

        verify(httpClient).deleteAll();
    }

    @Test
    void process_shouldInvokeClientDeleteById_whenServiceDeleteById() throws IOException, InterruptedException {
        doNothing().when(httpClient).deleteById(anyInt());

        service.deleteById(22);

        verify(httpClient).deleteById(anyInt());
    }

    @Test
    void process_shouldInvokeClientDeleteById_whenErrorOccursInDeleteById() throws IOException, InterruptedException {
        doThrow(InterruptedException.class).when(httpClient).deleteById(anyInt());

        service.deleteById(14);

        verify(httpClient).deleteById(anyInt());
    }

    @Test
    void process_shouldInvokeClientCreate_whenWeServiceCreate() throws IOException, InterruptedException {
        doNothing().when(httpClient).create(any(String.class));

        service.create(converter.convertToEntity(entityJson));

        verify(httpClient).create(any(String.class));
    }

    @Test
    void process_shouldInvokeClientCreate_whenWeServiceCreateOccursError() throws IOException, InterruptedException {
        doThrow(InterruptedException.class).when(httpClient).create(any(String.class));

        service.create(converter.convertToEntity(entityJson));

        verify(httpClient).create(any(String.class));
    }
}
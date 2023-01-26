package edu.geekhub.homework;

import edu.geekhub.homework.vehicle.VehicleGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static edu.geekhub.homework.RatRace.START;
import static edu.geekhub.homework.vehicle.VehicleType.CAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatRaceTest {
    @Spy
    private Random randomMock;

    private int roadSquareCount;

    private ScheduledExecutorService statisticScheduledExecutorSpy;

    private ScheduledExecutorService carScheduledExecutorSpy;

    private ScheduledExecutorService gameFinishedSchedulerExecutorSpy;

    private ExecutorService carExecutorServiceSpy;

    @Mock
    private AtomicBoolean gameFinished;

    @Mock
    private VehicleGenerator vehicleGeneratorMock;

    private RatRace ratRaceSpy;

    private RatRace ratRace;

    @BeforeEach
    void setUp() {
        roadSquareCount = 4;
        statisticScheduledExecutorSpy = spy(Executors.newSingleThreadScheduledExecutor());
        carScheduledExecutorSpy = spy(Executors.newSingleThreadScheduledExecutor());
        gameFinishedSchedulerExecutorSpy = spy(Executors.newSingleThreadScheduledExecutor());
        carExecutorServiceSpy = spy(Executors.newSingleThreadScheduledExecutor());

        ratRaceSpy = spy(new RatRace(randomMock,
                roadSquareCount,
                statisticScheduledExecutorSpy,
                carScheduledExecutorSpy,
                gameFinishedSchedulerExecutorSpy,
                carExecutorServiceSpy,
                vehicleGeneratorMock,
                gameFinished));

        ratRace = new RatRace(randomMock,
                roadSquareCount,
                statisticScheduledExecutorSpy,
                carScheduledExecutorSpy,
                gameFinishedSchedulerExecutorSpy,
                carExecutorServiceSpy,
                vehicleGeneratorMock,
                gameFinished);
    }

    @Test
    void process_shouldInitializeGameField_whenInitialize() {
        doNothing().when(ratRaceSpy).startProgram();
        ratRaceSpy.initializeGame();

        assertThat(Arrays.stream(ratRaceSpy
                        .getGameField())
                .flatMap(Arrays::stream)
                .collect(Collectors.toList())).allMatch((roadUnit -> roadUnit.status != 0));

        verify(ratRaceSpy, times(1)).buildRoad();
        verify(ratRaceSpy, times(1)).startProgram();
    }

    @Test
    void process_shouldReturnGameFieldString_whenInitializeGetGameFieldAsString() {
        doNothing().when(ratRaceSpy).startProgram();
        ratRaceSpy.initializeGame();

        assertThat(ratRaceSpy.getGameFieldAsString()).contains("█", "▒", "♦", "▒");
    }

    @Test
    void process_shouldReturnFreePoint_whenGetRandomFreePointInStartUnit() {
        doNothing().when(ratRaceSpy).startProgram();
        ratRaceSpy.initializeGame();

        Point point = ratRaceSpy.getRandomFreePointInStartUnit();
        assertThat(ratRaceSpy.getGameField()[point.y][point.x])
                .extracting(roadUnit -> roadUnit.status)
                .matches(num -> (num & START) != 0)
                .matches(num -> num < CAR.getStatus());
    }

    @Test
    void process_shouldInitializeRatRace_whenCallRatRaceWithOneParam() {
        RatRace ratRace = new RatRace(4);

        assertThat(ratRace.getGameField()).isNotNull();
    }
}
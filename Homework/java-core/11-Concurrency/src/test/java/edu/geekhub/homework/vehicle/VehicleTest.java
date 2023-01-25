package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.Point;
import edu.geekhub.homework.RoadUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicBoolean;

import static edu.geekhub.homework.RatRace.*;
import static edu.geekhub.homework.vehicle.VehicleType.CAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleTest {

    @Mock
    private RoadUnit unitMock;

    @Mock
    private AtomicBoolean atomicBooleanMock;

    RoadUnit[][] roadUnitsWithMock;

    private Vehicle carSpy;
    private Vehicle testCar;
    private Vehicle truckSpy;
    private Vehicle mopedSpy;
    private Point mockPoint;

    @BeforeEach
    void setUp() {
        roadUnitsWithMock = new RoadUnit[][]{new RoadUnit[]{unitMock}};
        mockPoint = new Point(0, 0);

        carSpy = spy(new Car("Test Car", 0, 0, roadUnitsWithMock, true, atomicBooleanMock));
        testCar = new Car("Test Car", 0, 0, roadUnitsWithMock, true, atomicBooleanMock);

        truckSpy = spy(new Truck("Test Truck", 0, 0, roadUnitsWithMock, true, atomicBooleanMock));
        mopedSpy = spy(new Moped("Test Moped", 0, 0, roadUnitsWithMock, true, atomicBooleanMock));

        carSpy.setDelay(1);
        truckSpy.setDelay(1);
        mopedSpy.setDelay(1);
    }

    @Test
    void process_returnVehicleProperties_whenGetValues() {
        assertThat(testCar.getX()).isZero();
        assertThat(testCar.getY()).isZero();
        assertThat(testCar.getName()).isEqualTo("Test Car");
        assertThat(testCar.getDelay()).isBetween(200L, 1000L);
        assertThat(testCar.toString()).contains("Vehicle{");
        assertThat(testCar.extractPoint()).isEqualTo(mockPoint);
        assertThat(carSpy.getType()).isEqualTo(CAR);
        assertThat(carSpy).isNotEqualTo(mopedSpy);

        testCar.setDelay(carSpy.getDelay());

        assertThat(testCar).isEqualTo(carSpy)
                .doesNotHaveSameHashCodeAs(carSpy);
    }

    @Test
    void process_shouldCallLogFallInAbyss_whenProcessFall() {
        doNothing().when(unitMock).unlock(any(Vehicle.class));
        unitMock.status = ABYSS;
        try (MockedStatic<Point> mocked = mockStatic(Point.class)) {
            mocked.when(() -> Point.getExistingNeighborPoint(anyInt(), anyInt(), anyInt(), anyInt()))
                    .thenReturn(mockPoint);

            carSpy.run();

            verify(carSpy, times(1)).logFallInAbyss(any(Vehicle.class), anyInt(), anyInt(), anyInt(), anyInt());
            verify(carSpy, times(1)).processFall(mockPoint);
            verify(carSpy, times(1)).setExist(false);
            verify(unitMock, times(2)).unlock(any(Vehicle.class));
        }
    }

    @Test
    void process_shouldCallLogCrash_whenProcessCrash() {
        doNothing().when(unitMock).unlock(any(Vehicle.class));
        doReturn(false).when(unitMock).tryJoin(any(Vehicle.class));
        doReturn(mopedSpy).when(unitMock).getVehicle();
        unitMock.status = ROAD;

        try (MockedStatic<Point> mocked = mockStatic(Point.class)) {
            mocked.when(() -> Point.getExistingNeighborPoint(anyInt(), anyInt(), anyInt(), anyInt()))
                    .thenReturn(mockPoint);

            carSpy.run();

            verify(carSpy, times(1)).logCrash(anyInt(), anyInt(), anyInt(), anyInt(), any(Vehicle.class), any(Vehicle.class));
            verify(carSpy, times(1)).processCrash(mockPoint);
            verify(carSpy, times(1)).setExist(false);
            verify(unitMock, times(2)).unlock(any(Vehicle.class));
        }
    }

    @Test
    void process_shouldCallLogMove_whenProcessMoveAndUnitNotFinish() {
        doNothing().when(unitMock).unlock(any(Vehicle.class));
        doReturn(true).
                doReturn(false).
                when(unitMock).tryJoin(any(Vehicle.class));
        doReturn(truckSpy).when(unitMock).getVehicle();
        unitMock.status = ROAD;

        try (MockedStatic<Point> mocked = mockStatic(Point.class)) {
            mocked.when(() -> Point.getExistingNeighborPoint(anyInt(), anyInt(), anyInt(), anyInt()))
                    .thenReturn(mockPoint);

            mopedSpy.run();

            verify(mopedSpy, times(1)).logMove(any(Vehicle.class), anyInt(), anyInt(), anyInt(), anyInt());
            verify(mopedSpy, times(1)).processMove(mockPoint);
            verify(unitMock, times(3)).unlock(any(Vehicle.class));
        }
    }

    @Test
    void process_shouldCallLogFinish_whenProcessMoveAndUnitIsFinish() {
        doNothing().when(unitMock).unlock(any(Vehicle.class));
        doNothing().when(atomicBooleanMock).set(anyBoolean());

        doReturn(true).when(unitMock).tryJoin(any(Vehicle.class));
        unitMock.status = FINISH;

        try (MockedStatic<Point> mocked = mockStatic(Point.class)) {
            mocked.when(() -> Point.getExistingNeighborPoint(anyInt(), anyInt(), anyInt(), anyInt()))
                    .thenReturn(mockPoint);

            carSpy.run();

            verify(carSpy, times(1)).logMove(any(Vehicle.class), anyInt(), anyInt(), anyInt(), anyInt());
            verify(carSpy, times(1)).logFinish(any(Vehicle.class), anyInt(), anyInt(), anyInt(), anyInt());
            verify(carSpy, times(1)).processMove(mockPoint);
            verify(carSpy, times(1)).setExist(false);
            verify(unitMock, times(2)).unlock(any(Vehicle.class));
            verify(atomicBooleanMock, times(1)).set(true);
        }
    }

    @Test
    void process_shouldCallOnlyUnlock_whenVehicleDoesntExist() {
        doNothing().when(unitMock).unlock(any(Vehicle.class));
        carSpy.setExist(false);

        try (MockedStatic<Point> mocked = mockStatic(Point.class)) {
            mocked.when(() -> Point.getExistingNeighborPoint(anyInt(), anyInt(), anyInt(), anyInt()))
                    .thenReturn(mockPoint);

            carSpy.run();


            verify(unitMock, only()).unlock(any(Vehicle.class));
            verify(carSpy, never()).processMove(any(Point.class));
            verify(carSpy, never()).processCrash(any(Point.class));
            verify(carSpy, never()).processFall(any(Point.class));
        }
    }
}
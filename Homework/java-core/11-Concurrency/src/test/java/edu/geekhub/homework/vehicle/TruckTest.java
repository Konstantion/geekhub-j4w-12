package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.Point;
import edu.geekhub.homework.RoadUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicBoolean;

import static edu.geekhub.homework.RatRace.ABYSS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TruckTest {
    @Mock
    private RoadUnit unitMock;

    @Mock
    private AtomicBoolean atomicBooleanMock;

    RoadUnit[][] roadUnitsWithMock;


    private Truck truckSpy;
    private Vehicle carSpy;

    private Point mockPoint;

    @BeforeEach
    void setUp() {
        roadUnitsWithMock = new RoadUnit[][]{new RoadUnit[]{unitMock}};
        mockPoint = new Point(0, 0);

        truckSpy = spy(new Truck("Test Truck", 0, 0, roadUnitsWithMock, true, atomicBooleanMock));
        carSpy = spy(new Car("Test Car", 0, 0, roadUnitsWithMock, true, atomicBooleanMock));

        truckSpy.setDelay(1L);
        truckSpy.setWaitingTime(1L);
        carSpy.setDelay(1L);
    }

    @Test
    void process_callUnlock_whenTruckDoesNotExist() {
        doNothing().when(unitMock).unlock(any(Vehicle.class));
        truckSpy.setExist(false);
        truckSpy.run();
        verify(unitMock, only()).unlock(any(Vehicle.class));
    }

    @Test
    void process_callProcessFall_whenUnitHasStatusAbyss() {
        doNothing().when(truckSpy).logFallInAbyss(any(Vehicle.class), anyInt(), anyInt(), anyInt(), anyInt());
        unitMock.status = ABYSS;

        try (MockedStatic<Point> mocked = mockStatic(Point.class)) {
            mocked.when(() -> Point.getExistingNeighborPoint(anyInt(), anyInt(), anyInt(), anyInt()))
                    .thenReturn(mockPoint);

            truckSpy.run();

            verify(truckSpy, times(1)).processFall(mockPoint);
            verify(truckSpy, times(1)).logFallInAbyss(any(Vehicle.class), anyInt(), anyInt(), anyInt(), anyInt());
            verify(truckSpy, times(1)).setExist(false);
            verify(unitMock, times(1)).unlock(any(Vehicle.class));
        }
    }

    @Test
    void process_callProcessMove_whenTryJoinTrue() {
        doNothing().when(truckSpy).logMove(any(Vehicle.class), anyInt(), anyInt(), anyInt(), anyInt());
        doNothing().when(unitMock).unlock(any(Vehicle.class));
        doReturn(true)
                .doReturn(false)
                .when(unitMock).tryJoin(any(Vehicle.class));
        doReturn(carSpy).when(unitMock).getVehicle();


        try (MockedStatic<Point> mocked = mockStatic(Point.class)) {
            mocked.when(() -> Point.getExistingNeighborPoint(anyInt(), anyInt(), anyInt(), anyInt()))
                    .thenReturn(mockPoint);

            truckSpy.run();

            verify(truckSpy, times(1)).processMove(mockPoint);
            verify(truckSpy, times(1)).setExist(false);
            verify(truckSpy, times(1)).logWaitFor(any(Vehicle.class), any(Point.class));
            verify(truckSpy, times(1)).logDeadLock(any(Vehicle.class), any(Point.class));
            verify(unitMock, atLeast(2)).tryJoin(any(Vehicle.class));
            verify(unitMock, times(3)).unlock(any(Vehicle.class));
            verify(unitMock, atLeast(2)).getVehicle();
        }
    }


}
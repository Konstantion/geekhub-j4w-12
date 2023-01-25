package edu.geekhub.homework;

import edu.geekhub.homework.vehicle.Car;
import edu.geekhub.homework.vehicle.Moped;
import edu.geekhub.homework.vehicle.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class RoadUnitTest {
    @Mock
    private Lock lockMock;

    private RoadUnit unit;

    @BeforeEach
    void setUp() {
        unit = new RoadUnit(lockMock, 0, 0, 1);
    }

    @Test
    void process_returnVehicle_whenSetGetVehicle() {
        RoadUnit manualUnit = new RoadUnit(0, 0, 1);
        Vehicle vehicle = new Car("Car", 0, 0, null, true, new AtomicBoolean(false));
        manualUnit.setVehicle(vehicle);

        assertThat(manualUnit.getVehicle()).isEqualTo(vehicle);
    }

    @Test
    void process_returnString_whenToString() {
        RoadUnit manualUnit = new RoadUnit(0, 0, 1);

        assertThat(manualUnit.toString())
                .contains("RoadUnit", "lock", "x", "y", "relToAbsFactor", "vehicle", "status");
    }

    @Test
    void process_returnFalse_whenTryJoinAndLockIsAlreadyJoined() {
        doReturn(false).when(lockMock).tryLock();

        Vehicle vehicle = new Car("Car", 0, 0, null, true, new AtomicBoolean(false));
        boolean actualResult = unit.tryJoin(vehicle);

        assertThat(actualResult).isFalse();
        assertThat(unit.getVehicle()).isNull();
    }

    @Test
    void process_returnTrueAndSetVehicle_whenTryJoinAndLockIsFree() {
        doReturn(true).when(lockMock).tryLock();

        Vehicle vehicle = new Moped("Moped", 0, 0, null, true, new AtomicBoolean(false));
        boolean actualResult = unit.tryJoin(vehicle);

        assertThat(actualResult).isTrue();
        assertThat(unit.getVehicle()).isEqualTo(vehicle);
    }

    @Test
    void process_doNotDoAnything_whenUnlockAndUnitVehicleNull() {
        unit.setVehicle(null);
        Vehicle vehicle = new Car("Car", 0, 0, null, true, new AtomicBoolean(false));

        unit.unlock(vehicle);

        verify(lockMock, never()).unlock();
    }

    @Test
    void process_shouldCallUnlock_whenVehicleIsNotNullAndSame() {
        doNothing().when(lockMock).unlock();
        Vehicle vehicle = new Car("Car", 0, 0, null, true, new AtomicBoolean(false));

        unit.setVehicle(vehicle);

        unit.unlock(vehicle);

        verify(lockMock, times(1)).unlock();
        assertThat(unit.getVehicle()).isNull();
    }
}
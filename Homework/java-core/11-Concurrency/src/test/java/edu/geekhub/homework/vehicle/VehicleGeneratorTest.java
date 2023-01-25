package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.Point;
import edu.geekhub.homework.RoadUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicBoolean;

import static edu.geekhub.homework.vehicle.VehicleType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleGeneratorTest {

    private VehicleGenerator vehicleGenerator;

    @Spy
    private AtomicBoolean atomicMock;

    private RoadUnit[][] roadUnits;

    @BeforeEach
    void setUp() {
        roadUnits = new RoadUnit[][]{};
        vehicleGenerator = new VehicleGenerator(roadUnits, atomicMock);
    }

    @Test
    void process_returnCarWithGivenCoordinates_whenGenerateRandomVehicleWithTypeCar() {
        int expectedX = 1;
        int expectedY = 1;
        try (MockedStatic<VehicleType> mockedStatic = mockStatic(VehicleType.class)) {
            mockedStatic.when(VehicleType::values).thenCallRealMethod();
            mockedStatic.when(VehicleType::getRandomType)
                    .thenReturn(CAR);

            Vehicle actualVehicle = vehicleGenerator
                    .generateRandomVehicle(expectedX, expectedY);

            assertThat(actualVehicle)
                    .extracting(Vehicle::getX).isEqualTo(expectedX);
            assertThat(actualVehicle)
                    .extracting(Vehicle::getY).isEqualTo(expectedY);
            assertThat(actualVehicle)
                    .extracting(Vehicle::getName).asString().contains("Car - 0");
            assertThat(actualVehicle).isInstanceOf(Car.class);
        }
    }

    @Test
    void process_returnTruckWithGivenCoordinates_whenGenerateRandomVehicleWithTypeTruck() {
        int expectedX = 1;
        int expectedY = 1;
        try (MockedStatic<VehicleType> mockedStatic = mockStatic(VehicleType.class)) {
            mockedStatic.when(VehicleType::values).thenCallRealMethod();
            mockedStatic.when(VehicleType::getRandomType)
                    .thenReturn(TRUCK);

            Vehicle actualVehicle = vehicleGenerator
                    .generateRandomVehicle(expectedX, expectedY);

            assertThat(actualVehicle)
                    .extracting(Vehicle::getX).isEqualTo(expectedX);
            assertThat(actualVehicle)
                    .extracting(Vehicle::getY).isEqualTo(expectedY);
            assertThat(actualVehicle)
                    .extracting(Vehicle::getName).asString().contains("Truck - 0");
            assertThat(actualVehicle).isInstanceOf(Truck.class);
        }
    }

    @Test
    void process_returnMopedWithGivenCoordinates_whenGenerateRandomVehicleWithTypeMoped() {
        int expectedX = 1;
        int expectedY = 1;
        try (MockedStatic<VehicleType> mockedStatic = mockStatic(VehicleType.class)) {
            mockedStatic.when(VehicleType::values).thenCallRealMethod();
            mockedStatic.when(VehicleType::getRandomType)
                    .thenReturn(MOPED);

            Vehicle actualVehicle = vehicleGenerator
                    .generateRandomVehicle(expectedX, expectedY);

            assertThat(actualVehicle)
                    .extracting(Vehicle::getX).isEqualTo(expectedX);
            assertThat(actualVehicle)
                    .extracting(Vehicle::getY).isEqualTo(expectedY);
            assertThat(actualVehicle)
                    .extracting(Vehicle::getName).asString().contains("Moped - 0");
            assertThat(actualVehicle).isInstanceOf(Moped.class);
        }
    }

    @Test
    void process_shouldCallGenerateRandomVehicleCoordinates_whenGenerateRandomVehiclePoint() {
        VehicleGenerator spyGenerator = spy(new VehicleGenerator(roadUnits, atomicMock));
        doReturn(null).when(spyGenerator).generateRandomVehicle(anyInt(), anyInt());

        Point actualPoint = new Point(0, 0);

        spyGenerator.generateRandomVehicle(actualPoint);

        verify(spyGenerator, times(1))
                .generateRandomVehicle(actualPoint.x, actualPoint.y);
    }
}
package edu.geekhub.homework.vehicle;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class VehicleTypeTest {
    @Test
    void process_returnVehicleType_whenGetRandomVehicleType() {
        VehicleType vehicleType = VehicleType.getRandomType();

        assertThat(VehicleType.values()).contains(vehicleType);
    }
}
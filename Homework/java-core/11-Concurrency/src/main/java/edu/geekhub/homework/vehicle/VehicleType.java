package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.Direction;

import java.util.Random;

public enum VehicleType {
    CAR(16),
    MOPED(32),
    TRUCK(64);

    private final int status;

    public static VehicleType getRandomType() {
        int pick = new Random().nextInt(VehicleType.values().length);
        return VehicleType.values()[pick];
    }

    VehicleType(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

package edu.geekhub.homework.vehicle;

public enum VehicaleType {
    CAR(16),
    MOPED(32),
    TRUCK(64);

    private final int status;

    VehicaleType(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

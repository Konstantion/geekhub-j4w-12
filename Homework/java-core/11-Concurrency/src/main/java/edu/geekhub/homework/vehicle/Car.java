package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.RoadUnit;

import java.util.concurrent.atomic.AtomicBoolean;

public class Car extends Vehicle {
    private static final int step = 1;

    public Car(String name, int x, int y, RoadUnit[][] gameField, boolean exist, AtomicBoolean gameFinished) {
        super(name, x, y, gameField, VehicleType.CAR, exist, step, gameFinished);
    }
}

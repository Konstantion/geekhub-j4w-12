package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.RoadUnit;

public class Car extends Vehicle {
    private static final int step = 1;

    public Car(String name, int x, int y, RoadUnit[][] gameField, boolean exist) {
        super(name, x, y, gameField, VehicleType.CAR, exist, step);
    }
}

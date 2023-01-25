package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.Point;
import edu.geekhub.homework.RoadUnit;

import java.util.concurrent.atomic.AtomicBoolean;

public class VehicleGenerator {
    private int vehicleId = 0;
    private final RoadUnit[][] gameField;
    private final AtomicBoolean gameFinished;

    public VehicleGenerator(RoadUnit[][] gameField, AtomicBoolean gameFinished) {
        this.gameField = gameField;
        this.gameFinished = gameFinished;
    }

    public Vehicle generateRandomVehicle(int x, int y) {
        VehicleType type = VehicleType.getRandomType();
        return switch (type) {
            case CAR -> new Car("Car - " + vehicleId++, x, y, gameField, true, gameFinished);
            case MOPED -> new Moped("Moped - " + vehicleId++, x, y, gameField, true, gameFinished);
            case TRUCK -> new Truck("Truck - " + vehicleId++, x, y, gameField, true, gameFinished);
        };
    }

    public Vehicle generateRandomVehicle(Point point) {
        return generateRandomVehicle(point.x, point.y);
    }
}

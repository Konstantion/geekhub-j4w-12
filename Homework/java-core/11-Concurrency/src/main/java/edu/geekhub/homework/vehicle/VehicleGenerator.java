package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.RoadUnit;

public class VehicleGenerator {
    private int vehicleId = 0;
    private final RoadUnit[][] gameField;

    public VehicleGenerator(RoadUnit[][] gameField) {
        this.gameField = gameField;
    }

    public Vehicle generateVehicle(int x, int y) {
        VehicleType type = VehicleType.getRandomType();
        return switch (type) {
            case CAR -> new Car("Car - " + vehicleId++, x, y, gameField, true);
            case MOPED -> new Moped("Moped - " + vehicleId++, x, y, gameField, true);
            case TRUCK -> new Truck("Truck - " + vehicleId++, x, y, gameField, true);
        };
    }
}

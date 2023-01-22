package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.RoadUnit;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class Moped extends Vehicle {
    private static final int step = 2;
    private final Logger logger = Logger.getLogger(Moped.class.getName());

    public Moped(String name, int x, int y,
                 RoadUnit[][] gameField,
                 boolean exist, AtomicBoolean gameFinished) {
        super(name, x, y, gameField, VehicleType.MOPED, exist, step, gameFinished);
    }
}

package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.RoadUnit;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class Moped extends Vehicle {
    private static final int step = 2;
    private final Logger logger = Logger.getLogger(Moped.class.getName());

    public Moped(String name, int x, int y, RoadUnit[][] gameField, boolean exist, AtomicBoolean gameFinished) {
        super(name, x, y, gameField, VehicleType.MOPED, exist, step, gameFinished);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Moped moped)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(logger, moped.logger);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), logger);
    }
}

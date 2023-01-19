package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.Direction;
import edu.geekhub.homework.Point;
import edu.geekhub.homework.RoadUnit;

import static edu.geekhub.homework.Point.getExistingNeighborCoordinates;

public class Car extends Vehicle {
    private static final int STEP = 1;

    public Car(String name, int x, int y, RoadUnit[][] gameField, boolean exist) {
        super(name, x, y, gameField, VehicaleType.CAR, exist);
    }

    @Override
    public void run() {
        while (exist) {
            synchronized (this) {
                try {
                    wait(delay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                Direction direction = Direction.getRandomDirection();
                x = Direction.moveX(x, direction);
                y = Direction.moveY(y, direction);

                gameField[y][x].join(this);
            }
        }
    }
}

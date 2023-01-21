package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.Point;
import edu.geekhub.homework.RoadUnit;

import java.util.logging.Level;

import static edu.geekhub.homework.Point.getExistingNeighborCoordinates;

public class Truck extends Vehicle {
    private static final int step = 1;

    public Truck(String name, int x, int y, RoadUnit[][] gameField, boolean exist) {
        super(name, x, y, gameField, VehicleType.TRUCK, exist, step);
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
                Point next = getExistingNeighborCoordinates(x, y, gameField.length, step);
                while (exist) {
                    if (gameField[next.y][next.x].tryJoin(this)) {
                        gameField[y][x].unlock();
                        logger.log(Level.FINE, "{0} go from x:{1} y:{2} to x:{3} y:{4}",
                                new Object[]{this.getName(), x, y, next.x, next.y});
                        x = next.x;
                        y = next.y;
                        break;
                    } else {
                        logger.log(Level.WARNING, "{0} waits to go to x:{1} y:{2}",
                                new Object[]{this.getName(), next.x, next.y});
                        try {
                            wait(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        }
        gameField[y][x].unlock();
    }
}


package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.Point;
import edu.geekhub.homework.RoadUnit;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import static edu.geekhub.homework.Point.getExistingNeighborCoordinates;
import static edu.geekhub.homework.RatRace.ABYSS;
import static edu.geekhub.homework.RatRace.FINISH;

public class Truck extends Vehicle {
    private static final int step = 1;

    public Truck(String name,
                 int x, int y,
                 RoadUnit[][] gameField,
                 boolean exist, AtomicBoolean gameFinished) {
        super(name, x, y, gameField, VehicleType.TRUCK, exist, step, gameFinished);
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
                    if ((gameField[next.y][next.x].status & ABYSS) != 0) {

                        logFallInAbyss(this, x, y, next.x, next.y);

                        this.exist = false;
                    }
                    if (gameField[next.y][next.x].tryJoin(this)) {

                        gameField[y][x].unlock();

                       logMove(this, x, y, next.x, next.y);

                        if ((gameField[next.y][next.x].status & FINISH) != 0) {
                            logFinish(this, x, y, next.x, next.y);
                            gameFinished.set(true);
                        }

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
        //Unlock unit if was crashed by another car
        gameField[y][x].unlock();
    }
}


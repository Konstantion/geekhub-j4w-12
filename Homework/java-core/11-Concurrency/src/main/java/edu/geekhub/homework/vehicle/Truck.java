package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.Point;
import edu.geekhub.homework.RoadUnit;

import java.util.concurrent.atomic.AtomicBoolean;

import static edu.geekhub.homework.Point.getExistingNeighborPoint;
import static edu.geekhub.homework.RatRace.ABYSS;

public class Truck extends Vehicle {
    private static final int step = 1;
    private volatile Point waitsFor = new Point(-1, -1);
    private static final int MAX_WAITING_COUNT = 20;

    public Truck(String name,
                 int x, int y,
                 RoadUnit[][] gameField,
                 boolean exist, AtomicBoolean gameFinished) {
        super(name, x, y, gameField, VehicleType.TRUCK, exist, step, gameFinished);
    }

    @Override
    public void run() {
        try {
            while (exist) {
                synchronized (this) {
                    wait(delay);
                    int waitingCount = 0;
                    Point next = Point.getExistingNeighborPoint(x, y, gameField.length, step);
                    while (exist) {
                        if ((gameField[next.y][next.x].status & ABYSS) != 0) {
                            processFall(next);
                        } else if (gameField[next.y][next.x].tryJoin(this)) {
                            gameField[y][x].unlock(this);
                            processMove(next);
                            break;
                        } else {
                            if (waitingCount == 0) {
                                logWaitFor(this, next);
                            }
                            waitingCount++;

                            if (waitingCount == MAX_WAITING_COUNT) {
                                gameField[y][x].unlock(this);
                                logDeadLock(this, next);
                                setExist(false);
                                gameField[next.y][next.x].getVehicle().setExist(false);
                            }
                            wait(200);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            //Unlock unit if was crashed by another car
            gameField[y][x].unlock(this);
        }
    }

    public Point getWaitsFor() {
        return waitsFor;
    }
}


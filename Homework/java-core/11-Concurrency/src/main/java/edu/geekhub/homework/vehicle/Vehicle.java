package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.Point;
import edu.geekhub.homework.RoadUnit;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.geekhub.homework.Point.getExistingNeighborCoordinates;
import static edu.geekhub.homework.RatRace.ABYSS;
import static edu.geekhub.homework.RatRace.FINISH;

public class Vehicle implements Runnable {
    protected static final long MIN_DELAY = 200;
    protected static final long MAX_DELAY = 1000;
    protected String name;
    protected int x;
    protected int y;
    protected final RoadUnit[][] gameField;
    protected VehicleType type;
    protected volatile boolean exist;
    protected long delay;
    protected final int step;
    protected final Logger logger = Logger.getLogger(Vehicle.class.getName());
    protected AtomicBoolean gameFinished;

    protected Vehicle(String name,
                      int x,
                      int y,
                      RoadUnit[][] gameField,
                      VehicleType type,
                      boolean exist,
                      int step,
                      AtomicBoolean gameFinished) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.gameField = gameField;
        this.type = type;
        this.exist = exist;
        this.step = step;
        this.gameFinished = gameFinished;
        delay = new Random().nextLong(MIN_DELAY, MAX_DELAY + 1);
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
                //car was crashed by another car while waiting
                if (!exist) {
                    break;
                }
                gameField[y][x].unlock();
                Point next = getExistingNeighborCoordinates(x, y, gameField.length, step);
                if ((gameField[next.y][next.x].status & ABYSS) != 0) {

                    logFallInAbyss(this, x, y, next.x, next.y);

                    this.exist = false;

                } else if (gameField[next.y][next.x].tryJoin(this)) {

                    logMove(this, x, y, next.x, next.y);

                    if ((gameField[next.y][next.x].status & FINISH) != 0) {
                        logFinish(this, x, y, next.x, next.y);
                        gameFinished.set(true);
                    }

                    x = next.x;
                    y = next.y;
                } else {
                    logCrash(x, y, next.x, next.y, this, gameField[next.y][next.x].getVehicle());
                    gameField[next.y][next.x].getVehicle().setExist(false);
                    this.exist = false;
                }
            }
        }
        //Unlock unit if was crashed by another car
        gameField[y][x].unlock();
    }

    public void logMove(Vehicle vehicle, int x, int y, int nextX, int nextY) {
        logger.log(Level.FINE, "{0} go from x:{1} y:{2} to x:{3} y:{4}",
                new Object[]{vehicle.getName(), x, y, nextX, nextY});
    }

    public void logCrash(int x, int y, int nextX, int nextY, Vehicle quilt, Vehicle victim) {
        logger.log(Level.WARNING,
                "Vehicle crash when go from x:{0} y:{1} to x:{2} y:{3}! {4}(quilt) and {5}(victim) ,crashed",
                new Object[]{x, y,
                        nextX, nextY,
                        quilt.getName(),
                        victim.getName()
                });
    }

    public void logFallInAbyss(Vehicle vehicle, int x, int y, int nextX, int nextY) {
        logger.log(Level.WARNING,
                "Vehicle {0} fall in the abyss when go from x:{1} y:{2}" +
                " to x:{3} y:{4}!",
                new Object[]{vehicle.getName(), x, y, nextX, nextY});
    }

    public void logFinish(Vehicle vehicle, int x, int y, int nextX, int nextY) {
        logger.log(Level.FINE, "{0} reached finish when go from x:{1} y:{2} to x:{3} y:{4}",
                new Object[]{vehicle.getName(), x, y, nextX, nextY});
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public RoadUnit[][] getGameField() {
        return gameField;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return x == vehicle.x && y == vehicle.y && exist == vehicle.exist && delay == vehicle.delay && Objects.equals(name, vehicle.name) && Arrays.equals(gameField, vehicle.gameField) && type == vehicle.type;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, x, y, type, exist, delay);
        result = 31 * result + Arrays.hashCode(gameField);
        return result;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
               "name='" + name + '\'' +
               ", x=" + x +
               ", y=" + y +
               ", type=" + type +
               ", exist=" + exist +
               ", delay=" + delay +
               '}';
    }
}

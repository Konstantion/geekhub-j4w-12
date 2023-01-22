package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.Point;
import edu.geekhub.homework.RoadUnit;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.geekhub.homework.Point.getExistingNeighborPoint;
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
        try {
            while (exist) {
                synchronized (this) {
                    wait(delay);
                    //car was crashed by another car while waiting
                    if (!exist) {
                        break;
                    }
                    gameField[y][x].unlock(this);
                    Point next = Point.getExistingNeighborPoint(x, y, gameField.length, step);
                    if ((gameField[next.y][next.x].status & ABYSS) != 0) {
                        processFall(next);
                    } else if (gameField[next.y][next.x].tryJoin(this)) {
                        processMove(next);
                    } else {
                        processCrash(next);
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

    public void logMove(Vehicle vehicle, int x, int y, int nextX, int nextY) {
        logger.log(Level.FINE, "{0} today at {1} go from x:{2} y:{3} to x:{4} y:{5}",
                new Object[]{
                        vehicle.getName(),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()),
                        x, y,
                        nextX, nextY
                });
    }

    public void logCrash(int x, int y, int nextX, int nextY, Vehicle quilt, Vehicle victim) {
        logger.log(Level.WARNING,
                "{0} crashed into {1} today at {2} when go from x:{3} y:{4} to x:{5} y:{6}!",
                new Object[]{
                        quilt.getName(),
                        victim.getName(),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()),
                        x, y,
                        nextX, nextY
                });
    }

    public void logFallInAbyss(Vehicle vehicle, int x, int y, int nextX, int nextY) {
        logger.log(Level.WARNING,
                "{0} fall in the abyss when go from x:{1} y:{2}" +
                " to x:{3} y:{4}! at {5}",
                new Object[]{
                        vehicle.getName(),
                        x, y,
                        nextX, nextY,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
                });
    }

    public void logFinish(Vehicle vehicle, int x, int y, int nextX, int nextY) {
        logger.log(Level.WARNING, "{0} REACHED FINISH WHEN GO FROM x:{1} y:{2} to x:{3} y:{4} at {5}",
                new Object[]{
                        vehicle.getName(),
                        x, y,
                        nextX, nextY,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
                });
    }

    public void logWaitFor(Vehicle vehicle, Point next) {
        logger.log(Level.WARNING, "{0} waits to go to x:{1} y:{2} at {3}",
                new Object[]{vehicle.getName(),
                        next.x, next.y,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())});
    }

    public void logDeadLock(Vehicle vehicle, Point next) {
        logger.log(Level.WARNING, "{0} and {1}, were removed because of dead lock at {2}",
                new Object[]{
                        vehicle.getName(),
                        gameField[next.y][next.x].getVehicle(),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
                });
    }

    protected void processFall(Point next) {
        logFallInAbyss(this, x, y, next.x, next.y);
        setExist(false);
    }

    protected void processMove(Point next) {
        logMove(this, x, y, next.x, next.y);

        if ((gameField[next.y][next.x].status & FINISH) != 0) {
            logFinish(this, x, y, next.x, next.y);

            setExist(false);

            gameFinished.set(true);
        }

        x = next.x;
        y = next.y;
    }

    protected void processCrash(Point next) {
        logCrash(x, y, next.x, next.y, this, gameField[next.y][next.x].getVehicle());
        gameField[next.y][next.x].getVehicle().setExist(false);
        setExist(false);
    }

    public String getName() {
        return name;
    }

    public VehicleType getType() {
        return type;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point extractPoint() {
        return new Point(x, y);
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

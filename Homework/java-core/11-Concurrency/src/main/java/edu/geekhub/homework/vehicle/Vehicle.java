package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.Point;
import edu.geekhub.homework.RoadUnit;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.geekhub.homework.Point.getExistingNeighborCoordinates;

public class Vehicle implements Runnable {
    protected static final long MIN_DELAY = 200;
    protected static final long MAX_DELAY = 1000;
    protected String name;
    protected int x;
    protected int y;
    protected final RoadUnit[][] gameField;
    protected VehicleType type;
    protected boolean exist;
    protected long delay;
    protected final int step;
    protected final Logger logger = Logger.getLogger(Vehicle.class.getName());

    protected Vehicle(String name,
                      int x,
                      int y,
                      RoadUnit[][] gameField,
                      VehicleType type,
                      boolean exist,
                      int step) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.gameField = gameField;
        this.type = type;
        this.exist = exist;
        this.step = step;
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
                if (exist) {
                    gameField[y][x].unlock();
                    Point next = getExistingNeighborCoordinates(x, y, gameField.length, step);
                    if (gameField[next.y][next.x].tryJoin(this)) {
                        logger.log(Level.FINE, "{0} go from x:{1} y:{2} to x:{3} y:{4}",
                                new Object[]{this.getName(), x, y, next.x, next.y});
                        x = next.x;
                        y = next.y;
                    } else {
                        logger.log(Level.WARNING, "Vehicle crash on x:{0} y:{1}," +
                                                  " {2}(quilt) and {3} ,crashed", new Object[]{next.x, next.y, this.getName(), gameField[next.y][next.x].getVehicle().getName()});
                        gameField[next.y][next.x].getVehicle().setExist(false);
                        this.exist = false;
                    }
                }
            }
        }
        gameField[y][x].unlock();
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

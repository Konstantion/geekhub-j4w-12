package edu.geekhub.homework.vehicle;

import edu.geekhub.homework.RoadUnit;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Vehicle implements Runnable {
    protected static final long MIN_DELAY = 200;
    protected static final long MAX_DELAY = 1000;
    protected String name;
    protected int x;
    protected int y;
    protected final RoadUnit[][] gameField;
    protected VehicaleType type;
    protected boolean exist;
    protected long delay;

    protected Vehicle(String name, int x, int y, RoadUnit[][] gameField, VehicaleType type, boolean exist) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.gameField = gameField;
        this.type = type;
        this.exist = exist;
        delay = new Random().nextLong(MIN_DELAY, MAX_DELAY + 1);
    }

    @Override
    public  void run() {

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

    public VehicaleType getType() {
        return type;
    }

    public void setType(VehicaleType type) {
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
               ", gameField=" + Arrays.toString(gameField) +
               ", type=" + type +
               ", exist=" + exist +
               ", delay=" + delay +
               '}';
    }
}

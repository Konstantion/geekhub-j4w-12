package edu.geekhub.homework;

import edu.geekhub.homework.vehicle.Vehicle;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.isNull;

public class RoadUnit {
    private final Lock lock = new ReentrantLock();
    private final int x;
    private final int y;
    private final int relToAbsFactor;
    private Vehicle vehicle;
    public int status;

    public RoadUnit(int x, int y, int relToAbsFactor) {
        this.x = x;
        this.y = y;
        this.relToAbsFactor = relToAbsFactor;
    }

    public boolean tryJoin(Vehicle vehicle) {
        if (!lock.tryLock()) {
            return false;
        }
        this.vehicle = vehicle;
        this.status |= vehicle.getType().getStatus();
        return true;
    }

    public void unlock(Vehicle caller) {
        if (!isNull(vehicle) &&
            vehicle.equals(caller)) {
            status -= vehicle.getType().getStatus();
            vehicle = null;
            lock.unlock();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRelX() {
        return x / relToAbsFactor;
    }

    public int getRelY() {
        return y / relToAbsFactor;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}

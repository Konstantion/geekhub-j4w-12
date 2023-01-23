package edu.geekhub.homework;

import edu.geekhub.homework.vehicle.Vehicle;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.isNull;

public class RoadUnit {
    private final Lock lock;
    private final int x;
    private final int y;
    private final int relToAbsFactor;
    private Vehicle vehicle;
    public int status;

    public RoadUnit(int x, int y, int relToAbsFactor) {
        lock = new ReentrantLock();
        this.x = x;
        this.y = y;
        this.relToAbsFactor = relToAbsFactor;
    }

    /**
     * {@deprecated}
     * Constructor with all dependency injected for tests,
     * it's {@code deprecated} and not recommended to use,
     * like constructor to create instance of the class,
     * to create optimised instance of the class use {@link #RoadUnit(int, int, int)}
     */
    @Deprecated
    public RoadUnit(Lock lock, int x, int y, int relToAbsFactor) {
        this.lock = lock;
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

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}

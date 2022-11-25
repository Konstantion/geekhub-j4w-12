package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.entity.Drone;

import java.util.Arrays;

import static java.util.Objects.isNull;

public class TrivialDroneCollection implements TrivialCollection {
    private Drone[] data;
    private int pointer = -1;
    private static final float EXTENSION_MULTIPLIER = 1.5f;

    public TrivialDroneCollection() {
        this(100);
    }

    public TrivialDroneCollection(int size) {
        data = new Drone[size];
    }

    @Override
    public void add(Object obj) {
        add(extractDrone(obj));
    }

    private void add(Drone drone) {
        pointer += 1;

        if (pointer == data.length - 1) {
            data = Arrays.copyOf(data, (int) (data.length * EXTENSION_MULTIPLIER + 1));
        }

        data[pointer] = drone;
    }

    private Drone extractDrone(Object obj) {
        if (isNull(obj)) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (obj instanceof Drone drone) {
            return drone;
        } else {
            throw new IllegalArgumentException("Object must be Drone");
        }
    }

    public Drone[] getData() {
        return Arrays.copyOf(data, length());
    }

    private int length() {
        return pointer + 1;
    }

    @Override
    public int count() {
        return length();
    }
}

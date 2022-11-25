package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.entity.Tank;

import java.util.Arrays;

import static java.util.Objects.isNull;

public class TrivialTankCollection implements TrivialCollection {
    private Tank[] data;
    private int pointer = -1;
    private static final float EXTENSION_MULTIPLIER = 1.5f;

    public TrivialTankCollection() {
        this(100);
    }

    public TrivialTankCollection(int size) {
        data = new Tank[size];
    }

    @Override
    public void add(Object obj) {
        add(extractTank(obj));
    }

    private void add(Tank tank) {
        pointer += 1;

        if (pointer == data.length - 1) {
            data = Arrays.copyOf(data, (int) (data.length * EXTENSION_MULTIPLIER + 1));
        }

        data[pointer] = tank;
    }

    private Tank extractTank(Object obj) {
        if (isNull(obj)) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (obj instanceof Tank tank) {
            return tank;
        } else {
            throw new IllegalArgumentException("Object must be Tank");
        }
    }

    public Tank[] getData() {
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

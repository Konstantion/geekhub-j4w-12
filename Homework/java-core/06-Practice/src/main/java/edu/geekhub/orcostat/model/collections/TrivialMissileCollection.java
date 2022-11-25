package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.entity.Missile;

import java.util.Arrays;

import static java.util.Objects.isNull;

public class TrivialMissileCollection implements TrivialCollection {
    private Missile[] data;
    private int pointer = -1;
    private static final float EXTENSION_MULTIPLIER = 1.5f;

    public TrivialMissileCollection() {
        this(100);
    }

    public TrivialMissileCollection(int size) {
        data = new Missile[size];
    }

    @Override
    public void add(Object obj) {
        add(extractMissile(obj));
    }

    private void add(Missile missile) {
        pointer += 1;

        if (pointer == data.length - 1) {
            data = Arrays.copyOf(data, (int) (data.length * EXTENSION_MULTIPLIER + 1));
        }

        data[pointer] = missile;
    }

    private Missile extractMissile(Object obj) {
        if (isNull(obj)) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (obj instanceof Missile missile) {
            return missile;
        } else {
            throw new IllegalArgumentException("Object must be Missile");
        }
    }

    public Missile[] getData() {
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

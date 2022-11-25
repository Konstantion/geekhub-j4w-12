package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.entity.Orc;

import java.util.Arrays;

import static java.util.Objects.isNull;

public class TrivialOrcCollection implements TrivialCollection {
    private Orc[] data;
    private int pointer = -1;
    private static final float EXTENSION_MULTIPLIER = 1.5f;

    public TrivialOrcCollection() {
        this(100);
    }

    public TrivialOrcCollection(int size) {
        data = new Orc[size];
    }

    @Override
    public void add(Object obj) {
        add(extractOrc(obj));
    }

    private void add(Orc orc) {
        pointer += 1;

        if (pointer == data.length - 1) {
            data = Arrays.copyOf(data, (int) (data.length * EXTENSION_MULTIPLIER + 1));
        }

        data[pointer] = orc;
    }

    private Orc extractOrc(Object obj) {
        if (isNull(obj)) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (obj instanceof Orc orc) {
            return orc;
        } else {
            throw new IllegalArgumentException("Object must be Orc");
        }

    }

    public Orc[] getData() {
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

package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.Orc;

import java.util.Arrays;

import static java.util.Objects.isNull;

public class TrivialOrcCollection implements TrivialCollection {
    private Orc[] data;
    private int POINTER = 0;
    private static final float EXTENSION_MULTIPLIER = 0.7f;

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
        if (isNull(orc)) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        if (POINTER == data.length - 1) {
            data = Arrays.copyOf(data, (int) (data.length * EXTENSION_MULTIPLIER + 1));
        }

        data[POINTER] = orc;
        POINTER += 1;
    }

    private Orc extractOrc(Object obj) {
        if (isNull(obj)) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (obj instanceof Orc orc) {
            add(orc);
        } else {
            throw new IllegalArgumentException("Object must be Orc");
        }
        return orc;
    }

    public Orc[] getData() {
        return data;
    }

    @Override
    public int count() {
        return POINTER + 1;
    }
}

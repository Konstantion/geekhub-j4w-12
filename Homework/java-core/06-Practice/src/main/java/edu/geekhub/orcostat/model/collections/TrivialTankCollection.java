package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.Orc;
import edu.geekhub.orcostat.model.Tank;

import java.util.Arrays;

import static java.util.Objects.isNull;

public class TrivialTankCollection implements TrivialCollection {
    private Tank[] data;
    private int POINTER = 0;
    private static final float EXTENSION_MULTIPLIER = 0.7f;

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
        if (POINTER == data.length - 1) {
            data = Arrays.copyOf(data, (int) (data.length * EXTENSION_MULTIPLIER + 1));
        }

        data[POINTER] = tank;
        POINTER += 1;
    }

    private Tank extractTank(Object obj) {
        if (isNull(obj)) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (obj instanceof Tank tank) {
            add(tank);
        } else {
            throw new IllegalArgumentException("Object must be Tank");
        }
        return tank;
    }

    public Tank[] getData() {
        return data;
    }

    @Override
    public int count() {
        return POINTER + 1;
    }
}

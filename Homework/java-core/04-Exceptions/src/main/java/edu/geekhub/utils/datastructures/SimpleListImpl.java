package edu.geekhub.utils.datastructures;

import java.util.Arrays;

public class SimpleListImpl implements SimpleList {
    private String[] array = new String[100];
    private final float EXTENSION_PERCENTAGE = 1.5f;
    private final float CAPACITY_PERCENTAGE = 0.7f;
    private int USED = -1;

    public SimpleListImpl(int size) {
        this.array = new String[size];
    }

    public SimpleListImpl() {

    }

    public int size() {
        return USED + 1;
    }
    public String lastAdded() {
        if(USED>=0) return array[USED];
        else throw new IndexOutOfBoundsException();
    }

    @Override
    public String get(int index) {
        if (!(index >= 0 && index <= USED)) {
            throw new IndexOutOfBoundsException();
        } else {
            return array[index];
        }
    }

    @Override
    public void add(String value) {
        if (USED + 1 > array.length * CAPACITY_PERCENTAGE) {
            array = Arrays.copyOf(array, (int) (array.length * EXTENSION_PERCENTAGE));
        }
        USED += 1;
        array[USED] = value;
    }

    @Override
    public void set(int index, String value) {
        if (!(index >= 0 && index <= USED)) {
            throw new IndexOutOfBoundsException();
        }
        array[index] = value;
    }

    @Override
    public void delete(int index) {
        if (!(index >= 0 && index <= USED)) {
            throw new IndexOutOfBoundsException();
        } else {
            array[index] = null;
            for (int i = index; i < USED; i++) {
                array[i] = array[i + 1];
            }
            array[USED] = null;
            USED -= 1;
        }
    }
}

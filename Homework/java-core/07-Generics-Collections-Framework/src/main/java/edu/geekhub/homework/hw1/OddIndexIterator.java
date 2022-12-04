package edu.geekhub.homework.hw1;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class OddIndexIterator<E> implements Iterator<E> {
    private final List<E> data;
    private int position = 1;
    private static final int ODD_STEP = 2;

    public OddIndexIterator(List<E> data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return position < data.size();
    }

    @Override
    public E next() {
        int i = position;
        if (i >= data.size()) {
            throw new NoSuchElementException();
        }
        position += ODD_STEP;
        return data.get(i);
    }
}

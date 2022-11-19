package edu.geekhub.homework.task3.utils;

import java.util.Arrays;

public class TriviaQueueImp implements TrivialStringQueue {
    private String[] data = new String[100];
    private int pointerQueue = 0;
    private int pointerDequeue = 0;
    private final float EXTENSION_PERCENTAGE = 1.5f;
    private final IllegalArgumentException STACK_IS_EMPTY = new IllegalArgumentException("Stack is empty");

    @Override
    public void push(String input) {
        if(pointerQueue == data.length) {
            data = Arrays.copyOf(data, (int) (data.length * EXTENSION_PERCENTAGE + 1));
        }
        data[pointerQueue] = input;
        pointerQueue += 1;
    }

    @Override
    public void push(String[] input) {
        for(String line : input) {
            push(line);
        }
    }

    @Override
    public String pop() {
        if(pointerDequeue >= pointerQueue) {
            throw STACK_IS_EMPTY;
        }

        String value = data[pointerDequeue];
        data[pointerDequeue] = null;
        pointerDequeue += 1;

        return value;
    }

    @Override
    public String peak() {
        if(pointerDequeue >= pointerQueue) {
            throw STACK_IS_EMPTY;
        }

        return data[pointerDequeue];
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean hasNext() {
        return pointerDequeue + 1 <= pointerQueue;
    }

    @Override
    public int size() {
        if(pointerDequeue > pointerQueue) {
            throw STACK_IS_EMPTY;
        }

        return pointerQueue - pointerDequeue;
    }

    public int indexDiff() {
        return pointerQueue - (pointerQueue - pointerDequeue);
    }
}

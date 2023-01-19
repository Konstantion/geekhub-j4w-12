package edu.geekhub.homework;

import java.util.Random;

public enum Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT;

    public static Direction getRandomDirection() {
        int pick = new Random().nextInt(Direction.values().length);
        return Direction.values()[pick];
    }

    public static int moveX(int x, Direction direction) {
        return switch (direction) {
            case LEFT -> x - 1;
            case RIGHT -> x + 1;
            default -> x;
        };
    }

    public static int moveY(int y, Direction direction) {
        return switch (direction) {
            case UP -> y - 1;
            case DOWN -> y + 1;
            default -> y;
        };
    }
}

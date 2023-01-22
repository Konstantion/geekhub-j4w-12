package edu.geekhub.homework;

import java.util.Objects;

import static edu.geekhub.homework.Util.checkIndex;
import static java.lang.Math.max;

public class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point getExistingNeighborPoint(int x, int y, int size) {
        return getExistingNeighborPoint(x, y, size, 1);
    }

    public static Point getNeighborPoint(int x, int y) {
        Direction direction = Direction.getRandomDirection();
        int neighborX = Direction.moveX(x, direction);
        int neighborY = Direction.moveY(y, direction);
        return new Point(neighborX, neighborY);
    }

    public static Point getExistingNeighborPoint(int x, int y, int size, int step) {
        if (size < x || size < y) {
            throw new IllegalArgumentException("Indexes should be less then size");
        }
        while (true) {
            step = max(step, 1);
            Direction direction = Direction.getRandomDirection();
            int neighborX = x;
            int neighborY = y;
            for (int i = 0; i < step; i++) {
                neighborX = Direction.moveX(neighborX, direction);
                neighborY = Direction.moveY(neighborY, direction);
            }
            if (checkIndex(neighborX, size) && checkIndex(neighborY, size)) {
                return new Point(neighborX, neighborY);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

package edu.geekhub.homework;

public class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point getExistingNeighborCoordinates(int x, int y, int size) {
        while (true) {
            Direction direction = Direction.getRandomDirection();
            int neighborX = Direction.moveX(x, direction);
            int neighborY = Direction.moveY(y, direction);
            if (!(
                    (neighborX < 0 || neighborY < 0)
                    ||
                    (neighborX >= size || neighborY >= size)
            )) {
                return new Point(neighborX, neighborY);
            }
        }
    }
}

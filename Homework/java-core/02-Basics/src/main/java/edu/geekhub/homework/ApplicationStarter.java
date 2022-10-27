package edu.geekhub.homework;

import static  java.lang.Math.PI;

public class ApplicationStarter {
    private static final double SECOND_POW = 2;
    private static final double ANGLE_OF_EQUILATERAL_TRIANGLE = 60;

    public static void main(String[] args) {
        double calculated = calculate(12);
    }

    private static double calculate(int n) {
        // Write code here :)
        return 0;
    }

    private static double calculateSquareAreaBySide(int side) {
        return Math.pow(side, SECOND_POW);
    }

    private static double calculateCircleAreaByRadius(int radius) {
        return PI * Math.pow(radius, SECOND_POW);
    }

    private static double calculateEquilateralTriangleAreaBySide(int side) {
        double h = side * Math.sin(Math.toRadians(ANGLE_OF_EQUILATERAL_TRIANGLE));
        return (side * h) / 2;
    }
}
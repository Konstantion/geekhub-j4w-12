package edu.geekhub.homework;

import static  java.lang.Math.PI;

public class ApplicationStarter {
    private static final double SECOND_POW = 2;
    private static final double ANGLE_OF_EQUILATERAL_TRIANGLE = 60;

    public static void main(String[] args) {
        int input = 9;
        double calculated = calculate(input);
        System.out.printf("For input value %d result is: %f", input, calculated);
    }

    private static double calculate(int n) {
        if (n % 2 == 0) {
            return calculateSquareAreaViaSide(n);
        } else if (n % 3 == 0) {
            return calculateCircleAreaViaRadius(n);
        } else {
            return calculateEquilateralTriangleAreaViaSide(n);
        }
    }

    private static double calculateSquareAreaViaSide(int side) {
        return Math.pow(side, SECOND_POW);
    }

    private static double calculateCircleAreaViaRadius(int radius) {
        return PI * Math.pow(radius, SECOND_POW);
    }

    private static double calculateEquilateralTriangleAreaViaSide(int side) {
        double height = side * Math.sin(Math.toRadians(ANGLE_OF_EQUILATERAL_TRIANGLE));
        return calculateTriangleAreaViaSideAndHeight(side, height);
    }

    private static double calculateTriangleAreaViaSideAndHeight(double side, double height) {
        return (side * height) / 2;
    }
}
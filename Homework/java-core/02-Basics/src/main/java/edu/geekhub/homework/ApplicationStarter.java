package edu.geekhub.homework;

import static  java.lang.Math.PI;

public class ApplicationStarter {
    public static void main(String[] args) {
        double calculated = calculate(12);
    }

    private static double calculate(int n) {
        // Write code here :)
        return 0;
    }

    private static double calculateSquareAreaBySide(int side) {
        return Math.pow(side, 2);
    }

    private static double calculateCircleAreaByRadius(int radius) {
        return PI * Math.pow(radius, 2);
    }
}
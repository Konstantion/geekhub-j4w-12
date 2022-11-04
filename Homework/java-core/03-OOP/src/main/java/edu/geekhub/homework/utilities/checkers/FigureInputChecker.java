package edu.geekhub.homework.utilities.checkers;

public class FigureInputChecker {
    public static boolean isTriangleExist(double sideA, double sideB, double sideC) {
        return isSideExist(sideA) && isSideExist(sideB)
                && isSideExist(sideC) && isTwoSideGreaterThanThird(sideA, sideB, sideC);
    }

    public static boolean isSideExist(double length) {
        return length > 0;
    }

    private static boolean isTwoSideGreaterThanThird(double sideA, double sideB, double sideC) {
        return (sideA + sideB > sideC) && (sideA + sideC > sideB) && (sideB + sideC > sideA);
    }

    private FigureInputChecker() {}
}

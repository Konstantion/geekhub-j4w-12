package edu.geekhub.homework.figures.comparators;

import edu.geekhub.homework.implementation.Figure;

public interface FigureComparator {

    int compare(Figure f1, Figure f2);

    static String convertComparatorResultToString(int result) {
        if (result > 0) {
            return "bigger";
        } else if (result < 0) {
            return "smaller";
        } else {
            return "equal";
        }
    }
}

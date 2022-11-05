package edu.geekhub.homework.figures.comparators;

import edu.geekhub.homework.implementation.Figure;

public class AreaComparator implements FigureComparator {
    @Override
    public int compare(Figure f1, Figure f2) {
        return Double.compare(f1.getArea(), f2.getArea());
    }
}

package edu.geekhub.homework.figures.comparators;

import edu.geekhub.homework.implementation.Figure;

public class PerimeterComparator implements FigureComparator {
    public final String parameter = "perimeter";

    @Override
    public int compare(Figure f1, Figure f2) {
        return Double.compare(f1.getPerimeter(), f2.getPerimeter());
    }
}

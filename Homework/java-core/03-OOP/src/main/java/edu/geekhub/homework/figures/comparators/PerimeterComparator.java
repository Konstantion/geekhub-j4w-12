package edu.geekhub.homework.figures.comparators;

import edu.geekhub.homework.implementation.Figure;
import java.util.Comparator;

public class PerimeterComparator implements FigureComparator {
    @Override
    public int compare(Figure f1, Figure f2) {
        return Double.compare(f1.getPerimeter(), f2.getPerimeter());
    }
}

package edu.geekhub.homework.interfaces;

import java.awt.Color;

public interface Colorable {
    void setColor(Color color);

    default Color getColor() {
        return Color.BLACK;
    }
}

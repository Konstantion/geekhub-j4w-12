package edu.geekhub.homework.interfaces;


import edu.geekhub.homework.enums.Color;

public interface Colorable {
    void setColor(Color color);

    default Color getColor() {
        return Color.BLACK;
    }
}

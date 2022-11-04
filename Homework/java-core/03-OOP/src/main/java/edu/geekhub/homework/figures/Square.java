package edu.geekhub.homework.figures;

import edu.geekhub.homework.enums.Color;
import edu.geekhub.homework.implementation.Figure;
import java.util.Locale;

public class Square extends Figure {
    private double side;
    private Color color;

    public Square(double side) {
        this.side = side;
        this.color = super.getColor();
    }

    public double getSide() {
        return side;
    }

    public void setSide(double side) {
        this.side = side;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format(
                "-a square%n"
                + "-area - %f%n"
                + "-perimeter - %f%n"
                + "-color - %s%n",
                getArea(),
                getPerimeter(),
                color.toString().toLowerCase(Locale.ROOT)
        );
    }

    @Override
    public double getPerimeter() {
        return side * 4;
    }

    @Override
    public double getArea() {
        return Math.pow(side, 2);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private double side;

        private Builder() {
        }

        public Builder side(double side) {
            this.side = side;
            return this;
        }

        public Square build() {
            return new Square(side);
        }
    }
}

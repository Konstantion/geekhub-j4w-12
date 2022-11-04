package edu.geekhub.homework.figures;

import edu.geekhub.homework.enums.Color;
import edu.geekhub.homework.implementation.Figure;
import java.util.Locale;

public class Circle extends Figure {
    private double radius;
    private Color color;

    public Circle(double radius) {
        this.radius = radius;
        this.color = super.getColor();
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return String.format(
            "-a circle%n"
            + "-area - %f%n"
            + "-perimeter - %f%n"
            + "-color - %s%n",
            getArea(),
            getPerimeter(),
            color.toString().toLowerCase(Locale.ROOT)
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private double radius;

        private Builder() {
        }

        public Builder radius(double radius) {
            this.radius = radius;
            return this;
        }

        public Circle build() {
            return new Circle(radius);
        }
    }
}

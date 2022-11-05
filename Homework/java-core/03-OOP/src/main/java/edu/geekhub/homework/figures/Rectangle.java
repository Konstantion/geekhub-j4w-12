package edu.geekhub.homework.figures;

import edu.geekhub.homework.enums.Color;
import edu.geekhub.homework.implementation.Figure;

import java.util.Locale;

public class Rectangle extends Figure {
    private double height;
    private double width;
    private Color color;

    public Rectangle(double height, double width) {
        this.height = height;
        this.width = width;
        this.color = super.getColor();
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public double getPerimeter() {
        return 2 * (height + width);
    }

    @Override
    public double getArea() {
        return height * width;
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
                "-a rectangle%n"
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
        private double height;
        private double width;

        private Builder() {
        }

        public Builder height(double height) {
            this.height = height;
            return this;
        }

        public Builder width(double width) {
            this.width = width;
            return this;
        }

        public Rectangle build() {
            return new Rectangle(height, width);
        }
    }
}

package edu.geekhub.homework.figures;

import edu.geekhub.homework.enums.Color;
import edu.geekhub.homework.implementation.Figure;
import java.util.Locale;

public class Triangle extends Figure {
    private double sideA;
    private double sideB;
    private double sideC;
    private Color color;

    public Triangle(double sideA, double sideB, double sideC) {
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
        this.color = super.getColor();
    }

    @Override
    public double getPerimeter() {
        return sideA + sideB + sideC;
    }

    @Override
    public double getArea() {
        double halfPerimeter = getPerimeter() / 2;
        return Math.sqrt(halfPerimeter * (halfPerimeter - sideA)
                * (halfPerimeter - sideB) * (halfPerimeter - sideC));
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public double getSideA() {
        return sideA;
    }

    public void setSideA(double sideA) {
        this.sideA = sideA;
    }

    public double getSideB() {
        return sideB;
    }

    public void setSideB(double sideB) {
        this.sideB = sideB;
    }

    public double getSideC() {
        return sideC;
    }

    public void setSideC(double sideC) {
        this.sideC = sideC;
    }

    @Override
    public String toString() {
        return String.format(
                "-a triangle%n"
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
        private double sideA;
        private double sideB;
        private double sideC;

        private Builder() {
        }

        public Builder sideA(double sideA) {
            this.sideA = sideA;
            return this;
        }

        public Builder sideB(double sideB) {
            this.sideB = sideB;
            return this;
        }

        public Builder sideC(double sideC) {
            this.sideC = sideC;
            return this;
        }

        public Triangle build() {
            return new Triangle(sideA, sideB, sideC);
        }
    }
}

package edu.geekhub.homework;

import org.junit.jupiter.api.Test;

import static java.lang.Math.abs;
import static java.lang.Math.max;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PointTest {

    @Test
    void process_returnPointWithGivenCoordinates_whenCreateNewPoint() {
        int expectedX = 1;
        int expectedY = 1;
        Point point = new Point(expectedX, expectedY);

        assertThat(point.x).isEqualTo(expectedX);
        assertThat(point.y).isEqualTo(expectedY);
    }

    @Test
    void process_returnNeighborPoint_whenGetExistingNeighborCoordinates() {
        int x = 1;
        int y = 5;
        int size = 10;
        Point point = Point.getExistingNeighborPoint(x, y, size);
        int expectedDifference = 1;

        int xCordDifference = abs(x - point.x);
        int yCordDifference = abs(y - point.y);
        int actualDifference = max(xCordDifference, yCordDifference);

        assertThat(actualDifference).isEqualTo(expectedDifference);
    }

    @Test
    void process_throwException_whenGivenSizeLessThanCoordinate() {
        int x = 1;
        int y = 5;
        int size = 2;
        assertThatThrownBy(() -> Point.getExistingNeighborPoint(x, y, size))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Indexes should be less then size");
    }

    @Test
    void process_throwException_whenGivenIndexLessThanZero() {
        int x = -1;
        int y = 5;
        int size = 10;
        assertThatThrownBy(() -> Point.getExistingNeighborPoint(x, y, size))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Indexes should be bigger or equal to zero");
    }

    @Test
    void process_returnNeighborPointWithStep_whenGetExistingNeighborCoordinates() {
        int x = 1;
        int y = 5;
        int size = 10;
        int step = 2;
        Point point = Point.getExistingNeighborPoint(x, y, size, step);

        int expectedDifference = step;

        int xCordDifference = abs(x - point.x);
        int yCordDifference = abs(y - point.y);
        int actualDifference = max(xCordDifference, yCordDifference);

        assertThat(actualDifference).isEqualTo(expectedDifference);
    }

    @Test
    void process_shouldReturnTrue_whenCompareToEqualPoints() {
        Point pointOne = new Point(0, 0);
        Point pointTwo = new Point(0, 0);

        assertThat(pointOne)
                .hasSameHashCodeAs(pointTwo)
                .isEqualTo(pointTwo);
    }

    @Test
    void process_returnNeighborPoint_whenGetExistingNeighborPointWithNegativeCoordinates() {
        int x = -184327589;
        int y = 5;
        int size = 10;
        Point point = Point.getNeighborPoint(x, y);
        int expectedDifference = 1;

        int xCordDifference = abs(x - point.x);
        int yCordDifference = abs(y - point.y);
        int actualDifference = max(xCordDifference, yCordDifference);

        assertThat(actualDifference).isEqualTo(expectedDifference);
    }
}
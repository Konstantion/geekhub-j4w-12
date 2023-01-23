package edu.geekhub.homework;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DirectionTest {

    @Test
    void process_shouldReturnDirection_whenGetRandomDirection() {
        Direction direction = Direction.getRandomDirection();

        assertThat(direction).isInstanceOf(Direction.class);
    }

    @Test
    void process_shouldReturnMovedX_whenMoveXWithLeftOrRight() {
        Direction right = Direction.RIGHT;
        Direction left = Direction.LEFT;
        int x = 0;

        int expectedMovedRight = x + 1;
        int expectedMovedLeft = x - 1;

        int actualMovedLeft = Direction.moveX(x, left);
        int actualMovedRight = Direction.moveX(x, right);

        assertThat(actualMovedLeft).isEqualTo(expectedMovedLeft);
        assertThat(actualMovedRight).isEqualTo(expectedMovedRight);
    }

    @Test
    void process_shouldReturnNotMovedX_whenMoveXWithUpOrDown() {
        Direction right = Direction.RIGHT;
        Direction left = Direction.LEFT;
        int y = 0;


        int actualMovedLeft = Direction.moveY(y, left);
        int actualMovedRight = Direction.moveY(y, right);

        assertThat(actualMovedLeft).isEqualTo(y);
        assertThat(actualMovedRight).isEqualTo(y);
    }

    @Test
    void process_shouldReturnNotMovedY_whenMoveYWithLeftOrRight() {
        Direction up = Direction.UP;
        Direction down = Direction.DOWN;
        int x = 0;

        int actualMovedUp = Direction.moveX(x, up);
        int actualMovedDown = Direction.moveX(x, down);

        assertThat(actualMovedUp).isEqualTo(x);
        assertThat(actualMovedDown).isEqualTo(x);
    }

    @Test
    void process_shouldReturnMovedY_whenMoveYWithUpOrDown() {
        Direction up = Direction.UP;
        Direction down = Direction.DOWN;
        int y = 0;

        int expectedMovedUp = y - 1;
        int expectedMovedDown = y + 1;

        int actualMovedUp = Direction.moveY(y, up);
        int actualMovedDown = Direction.moveY(y, down);

        assertThat(actualMovedUp).isEqualTo(expectedMovedUp);
        assertThat(actualMovedDown).isEqualTo(expectedMovedDown);
    }
}
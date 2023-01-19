package edu.geekhub.homework;

import java.security.SecureRandom;
import java.util.Random;

import static edu.geekhub.homework.Point.getExistingNeighborCoordinates;
import static java.lang.Math.floor;
import static java.lang.Math.pow;

public class RatRace {
    private final RoadUnit[][] gameField;
    private final Random random;
    public final int relativeSize;
    public final int absoluteSize;
    public final int roadSquareCount;
    public static final int REL_TO_ABS_FACTOR = 4;

    public static final int ROAD = 1;
    public static final int START = 2;
    public static final int FINISH = 4;
    public static final int ABYSS = 8;

    public RatRace(int roadSquareCount) {
        this(roadSquareCount, new SecureRandom());
    }

    public RatRace(int roadSquareCount, Random random) {
        roadSquareCount = Math.max(roadSquareCount, 3);
        this.roadSquareCount = roadSquareCount;
        this.random = random;
        relativeSize = (int) (floor(pow(roadSquareCount, 0.5)) + 1);
        absoluteSize = relativeSize * REL_TO_ABS_FACTOR;
        gameField = new RoadUnit[absoluteSize][absoluteSize];

        initializeGameField();
    }

    private void initializeGameField() {
        for (int y = 0; y < absoluteSize; y++) {
            for (int x = 0; x < absoluteSize; x++) {
                gameField[y][x] = new RoadUnit(x, y, REL_TO_ABS_FACTOR);
                gameField[y][x].status = ABYSS;
            }
        }

        int absCurrX = random.nextInt(relativeSize);
        int absCurrY = random.nextInt(relativeSize);
        replaceStatusToRoadSquare(absCurrX, absCurrY, START);
        int roadSquaresLeft = roadSquareCount - 1;
        while (roadSquaresLeft > 0) {
            Point neighborCoordinates =
                    getExistingNeighborCoordinates(absCurrX, absCurrY, relativeSize);
            int absTempX = neighborCoordinates.x * REL_TO_ABS_FACTOR;
            int absTempY = neighborCoordinates.y * REL_TO_ABS_FACTOR;
            int relTempX = neighborCoordinates.x;
            int relTempY = neighborCoordinates.y;
            if ((gameField[absTempY][absTempX].status & START) != 0) {
                continue;
            }
            if (roadSquaresLeft == 1) {
                replaceStatusToRoadSquare(relTempX, relTempY, FINISH);
            } else {
                replaceStatusToRoadSquare(relTempX, relTempY, ROAD);
            }
            absCurrX = relTempX;
            absCurrY = relTempY;
            roadSquaresLeft--;
        }
    }

    private void addStatusToRoadSquare(int relevantX, int relevantY, int status) {
        int absolutX = relevantX * REL_TO_ABS_FACTOR;
        int absolutY = relevantY * REL_TO_ABS_FACTOR;
        for (int y = absolutY; y < absolutY + REL_TO_ABS_FACTOR; y++) {
            for (int x = absolutX; x < absolutX + REL_TO_ABS_FACTOR; x++) {
                gameField[y][x].status |= status;
            }
        }
    }

    private void replaceStatusToRoadSquare(int relevantX, int relevantY, int status) {
        int absolutX = relevantX * REL_TO_ABS_FACTOR;
        int absolutY = relevantY * REL_TO_ABS_FACTOR;
        for (int y = absolutY; y < absolutY + REL_TO_ABS_FACTOR; y++) {
            for (int x = absolutX; x < absolutX + REL_TO_ABS_FACTOR; x++) {
                gameField[y][x].status = status;
            }
        }
    }

    private void removeStatusFromRoadSquare(int relevantX, int relevantY, int status) {
        int absolutX = relevantX * REL_TO_ABS_FACTOR;
        int absolutY = relevantY * REL_TO_ABS_FACTOR;
        for (int y = absolutY; y < absolutY + REL_TO_ABS_FACTOR; y++) {
            for (int x = absolutX; x < absolutX + REL_TO_ABS_FACTOR; x++) {
                gameField[y][x].status -= status;
            }
        }
    }

    public String getGameFieldAsString() {
        StringBuilder fieldBuilder = new StringBuilder();
        for (int y = 0; y < absoluteSize; y++) {
            for (int x = 0; x < absoluteSize; x++) {
                fieldBuilder.append(gameField[y][x].status);
                if (x % REL_TO_ABS_FACTOR == REL_TO_ABS_FACTOR - 1) {
                    fieldBuilder.append("|");
                }
            }
            fieldBuilder.append("\n");
            if (y % REL_TO_ABS_FACTOR == REL_TO_ABS_FACTOR - 1) {
                fieldBuilder.append("—".repeat(absoluteSize + relativeSize - 1));
                fieldBuilder.append("\n");
            }

        }
        return fieldBuilder.toString();
    }
}

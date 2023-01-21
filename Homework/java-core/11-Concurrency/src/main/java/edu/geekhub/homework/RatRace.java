package edu.geekhub.homework;

import edu.geekhub.homework.vehicle.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static edu.geekhub.homework.Point.getExistingNeighborCoordinates;
import static java.lang.Math.floor;
import static java.lang.Math.pow;

public class RatRace {
    private final RoadUnit[][] gameField;
    private final Random random;
    public final int relativeSize;
    public final int absoluteSize;
    public final int roadSquareCount;

    public static final int REL_TO_ABS_FACTOR = 3;
    public static final int ROAD = 1;
    public static final int START = 2;
    public static final int FINISH = 4;
    public static final int ABYSS = 8;

    private int carCount = 0;

    private final ScheduledExecutorService statisticScheduledExecutor;
    private final ScheduledExecutorService carScheduledExecutor;
    private final ExecutorService carExecutorService;

    private final VehicleGenerator vehicleGenerator;


    public RatRace(int roadSquareCount) {
        this(roadSquareCount, new SecureRandom(), Executors.newSingleThreadScheduledExecutor());
    }

    public RatRace(int roadSquareCount, ScheduledExecutorService statisticScheduledExecutor) {
        this(roadSquareCount, new SecureRandom(), statisticScheduledExecutor);
    }

    public RatRace(int roadSquareCount, Random random, ScheduledExecutorService statisticScheduledExecutor) {
        this.statisticScheduledExecutor = statisticScheduledExecutor;
        carScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        carExecutorService = Executors.newFixedThreadPool(10);

        roadSquareCount = Math.max(roadSquareCount, 3);
        this.roadSquareCount = roadSquareCount;
        this.random = random;
        relativeSize = (int) (floor(pow(roadSquareCount, 0.5)) + 1);
        absoluteSize = relativeSize * REL_TO_ABS_FACTOR;
        gameField = new RoadUnit[absoluteSize][absoluteSize];
        vehicleGenerator = new VehicleGenerator(gameField);
        initializeGameField();
    }

    private void initializeGameField() {
        for (int y = 0; y < absoluteSize; y++) {
            for (int x = 0; x < absoluteSize; x++) {
                gameField[y][x] = new RoadUnit(x, y, REL_TO_ABS_FACTOR);
                gameField[y][x].status = ABYSS;
            }
        }
        buildRoad();
        statisticScheduledExecutor.scheduleAtFixedRate(() -> System.out.println(
                        getGameFieldWithCarsAsString()
                ),
                1000,
                1000,
                TimeUnit.MILLISECONDS);
        carScheduledExecutor.scheduleAtFixedRate(
                () -> {
                    carExecutorService.submit(vehicleGenerator.generateVehicle(0, 0));
                },
                1000,
                4000,
                TimeUnit.MILLISECONDS);
//        carExecutorService.submit(new Truck("Truck - " + carCount++,
//                0, 0,
//                gameField,
//                true));
//        carExecutorService.submit(new Truck("Truck - " + carCount++,
//                3, 3,
//                gameField,
//                true));
//        carExecutorService.submit(new Car("Car - " + carCount++,
//                2, 2,
//                gameField,
//                true));
    }

    private void buildRoad() {
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

    private void replaceStatusToRoadSquare(int relevantX, int relevantY, int status) {
        int absolutX = relevantX * REL_TO_ABS_FACTOR;
        int absolutY = relevantY * REL_TO_ABS_FACTOR;
        for (int y = absolutY; y < absolutY + REL_TO_ABS_FACTOR; y++) {
            for (int x = absolutX; x < absolutX + REL_TO_ABS_FACTOR; x++) {
                gameField[y][x].status = status;
            }
        }
    }

    public String getGameFieldAsString() {
        StringBuilder fieldBuilder = new StringBuilder();
        fieldBuilder
                .append(LocalDateTime.now().getHour())
                .append(":")
                .append(LocalDateTime.now().getMinute())
                .append(":")
                .append(LocalDateTime.now().getSecond()).append("\n");
        fieldBuilder.append("+")
                .append("—".repeat(absoluteSize + relativeSize - 1))
                .append("+")
                .append("\n");
        for (int y = 0; y < absoluteSize; y++) {
            fieldBuilder.append("|");
            for (int x = 0; x < absoluteSize; x++) {
                fieldBuilder.append(gameField[y][x].status);
                if (x % REL_TO_ABS_FACTOR == REL_TO_ABS_FACTOR - 1) {
                    fieldBuilder.append("|");
                }
            }
            fieldBuilder.append("\n");
            if (y % REL_TO_ABS_FACTOR == REL_TO_ABS_FACTOR - 1) {
                fieldBuilder.append("+");
                fieldBuilder.append("—".repeat(absoluteSize + relativeSize - 1));
                fieldBuilder.append("+");
                fieldBuilder.append("\n");
            }

        }
        return fieldBuilder.toString();
    }

    public String getGameFieldWithTrackingStatusAsString(int status) {
        StringBuilder fieldBuilder = new StringBuilder();
        fieldBuilder
                .append(LocalDateTime.now().getHour())
                .append(":")
                .append(LocalDateTime.now().getMinute())
                .append(":")
                .append(LocalDateTime.now().getSecond()).append("\n");
        fieldBuilder.append("+")
                .append("—".repeat(absoluteSize + relativeSize - 1))
                .append("+")
                .append("\n");
        for (int y = 0; y < absoluteSize; y++) {
            fieldBuilder.append("|");
            for (int x = 0; x < absoluteSize; x++) {
                String currentSymbol = " ";
                if ((gameField[y][x].status & status) != 0) {
                    currentSymbol = "♣";
                }
                fieldBuilder.append(currentSymbol);
                if (x % REL_TO_ABS_FACTOR == REL_TO_ABS_FACTOR - 1) {
                    fieldBuilder.append("|");
                }
            }
            fieldBuilder.append("\n");
            if (y % REL_TO_ABS_FACTOR == REL_TO_ABS_FACTOR - 1) {
                fieldBuilder.append("+");
                fieldBuilder.append("—".repeat(absoluteSize + relativeSize - 1));
                fieldBuilder.append("+");
                fieldBuilder.append("\n");
            }

        }
        return fieldBuilder.toString();
    }

    public String getGameFieldWithCarsAsString() {
        StringBuilder fieldBuilder = new StringBuilder();
        fieldBuilder
                .append(LocalDateTime.now().getHour())
                .append(":")
                .append(LocalDateTime.now().getMinute())
                .append(":")
                .append(LocalDateTime.now().getSecond()).append("\n");
        fieldBuilder.append("+")
                .append("—".repeat(absoluteSize + relativeSize - 1))
                .append("+")
                .append("\n");
        for (int y = 0; y < absoluteSize; y++) {
            fieldBuilder.append("|");
            for (int x = 0; x < absoluteSize; x++) {
                String currentSymbol = " ";
                if ((gameField[y][x].status & VehicleType.CAR.getStatus()) != 0) {
                    currentSymbol = "♣";
                }
                if ((gameField[y][x].status & VehicleType.MOPED.getStatus()) != 0) {
                    currentSymbol = "♦";
                }
                if ((gameField[y][x].status & VehicleType.TRUCK.getStatus()) != 0) {
                    currentSymbol = "♠";
                }
                fieldBuilder.append(currentSymbol);
                if (x % REL_TO_ABS_FACTOR == REL_TO_ABS_FACTOR - 1) {
                    fieldBuilder.append("|");
                }
            }
            fieldBuilder.append("\n");
            if (y % REL_TO_ABS_FACTOR == REL_TO_ABS_FACTOR - 1) {
                fieldBuilder.append("+");
                fieldBuilder.append("—".repeat(absoluteSize + relativeSize - 1));
                fieldBuilder.append("+");
                fieldBuilder.append("\n");
            }

        }
        return fieldBuilder.toString();
    }
}
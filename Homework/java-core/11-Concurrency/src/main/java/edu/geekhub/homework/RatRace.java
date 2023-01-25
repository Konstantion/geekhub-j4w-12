package edu.geekhub.homework;

import edu.geekhub.homework.vehicle.VehicleGenerator;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static edu.geekhub.homework.Point.getExistingNeighborPoint;
import static edu.geekhub.homework.Point.getNeighborPoint;
import static edu.geekhub.homework.vehicle.VehicleType.*;
import static java.lang.Math.*;

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

    private int startX;
    private int startY;

    private AtomicBoolean gameFinished = new AtomicBoolean(false);

    private final ScheduledExecutorService statisticScheduledExecutor;
    private final ScheduledExecutorService carScheduledExecutor;
    private final ScheduledExecutorService gameFinishedSchedulerExecutor;

    private final ExecutorService carExecutorService;

    private final VehicleGenerator vehicleGenerator;


    public RatRace(int roadSquareCount) {
        this(roadSquareCount, new SecureRandom(), Executors.newSingleThreadScheduledExecutor());
    }

    private RatRace(int roadSquareCount, Random random, ScheduledExecutorService statisticScheduledExecutor) {
        this.statisticScheduledExecutor = statisticScheduledExecutor;
        carScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        gameFinishedSchedulerExecutor = Executors.newSingleThreadScheduledExecutor();
        carExecutorService = Executors.newFixedThreadPool(
                min(roadSquareCount * REL_TO_ABS_FACTOR, 30)
        );

        roadSquareCount = Math.max(roadSquareCount, 3);

        this.roadSquareCount = roadSquareCount;
        this.random = random;

        relativeSize = (int) (floor(pow(roadSquareCount, 0.5)) + 1);
        absoluteSize = relativeSize * REL_TO_ABS_FACTOR;

        gameField = new RoadUnit[absoluteSize][absoluteSize];
        vehicleGenerator = new VehicleGenerator(gameField, gameFinished);

        initializeGame();
    }

    /**
     * {@deprecated}
     * Constructor with all dependency injected for tests,
     * it's  {@code deprecated} and not recommended to use,
     * like constructor to create instance of the class,
     * to create optimised instance of the class use {@link #RatRace(int)}
     */
    @Deprecated
    public RatRace(Random random,
                   int roadSquareCount,
                   ScheduledExecutorService statisticScheduledExecutor,
                   ScheduledExecutorService carScheduledExecutor,
                   ScheduledExecutorService gameFinishedSchedulerExecutor,
                   ExecutorService carExecutorService,
                   VehicleGenerator vehicleGenerator
    ) {
        this.statisticScheduledExecutor = statisticScheduledExecutor;
        this.carScheduledExecutor = carScheduledExecutor;
        this.gameFinishedSchedulerExecutor = gameFinishedSchedulerExecutor;
        this.carExecutorService = carExecutorService;
        this.vehicleGenerator = vehicleGenerator;

        roadSquareCount = Math.max(roadSquareCount, 3);

        this.roadSquareCount = roadSquareCount;
        this.random = random;

        relativeSize = (int) (floor(pow(roadSquareCount, 0.5)) + 1);
        absoluteSize = relativeSize * REL_TO_ABS_FACTOR;

        gameField = new RoadUnit[absoluteSize][absoluteSize];
        vehicleGenerator = new VehicleGenerator(gameField, gameFinished);

        initializeGame();
    }

    private void initializeGame() {
        for (int y = 0; y < absoluteSize; y++) {
            for (int x = 0; x < absoluteSize; x++) {
                gameField[y][x] = new RoadUnit(x, y, REL_TO_ABS_FACTOR);
                gameField[y][x].status = ABYSS;
            }
        }

        buildRoad();

        startProgram();
    }

    private void startProgram() {
        statisticScheduledExecutor.scheduleAtFixedRate(() -> System.out.println(
                        getGameFieldAsStringTest()
                ),
                1000,
                1000,
                TimeUnit.MILLISECONDS);
        carScheduledExecutor.scheduleAtFixedRate(
                () -> carExecutorService.submit(vehicleGenerator.generateRandomVehicle(
                        getRandomFreePointInStartUnit())),
                1000,
                1000,
                TimeUnit.MILLISECONDS);

        gameFinishedSchedulerExecutor.scheduleAtFixedRate(() -> {
                    if (gameFinished.get()) {
                        statisticScheduledExecutor.shutdownNow();
                        carScheduledExecutor.shutdownNow();
                        carExecutorService.shutdownNow();
                        gameFinishedSchedulerExecutor.shutdownNow();
                    }
                },
                1000,
                300,
                TimeUnit.MILLISECONDS
        );
    }

    private void buildRoad() {
        int absCurrX = random.nextInt(relativeSize);
        int absCurrY = random.nextInt(relativeSize);
        replaceStatusToRoadSquare(absCurrX, absCurrY, START);
        startX = absCurrX;
        startY = absCurrY;
        int roadSquaresLeft = roadSquareCount - 1;
        while (roadSquaresLeft > 0) {
            Point neighborCoordinates =
                    getExistingNeighborPoint(absCurrX, absCurrY, relativeSize);
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

    private Point getRandomFreePointInStartUnit() {
        int absStartX = startX * REL_TO_ABS_FACTOR;
        int absStartY = startY * REL_TO_ABS_FACTOR;
        int unitHalf = REL_TO_ABS_FACTOR / 2;
        while (true) {
            Point point = getNeighborPoint(
                    absStartX + unitHalf,
                    absStartY + unitHalf
            );
            if (gameField[point.y][point.x].status < CAR.getStatus()) {
                return point;
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

    public String getGameFieldAsStringTest() {
        StringBuilder fieldBuilder = new StringBuilder();
        fieldBuilder
                .append(LocalDateTime.now().getHour())
                .append(":")
                .append(LocalDateTime.now().getMinute())
                .append(":")
                .append(LocalDateTime.now().getSecond()).append("\n");
        for (int y = 0; y < absoluteSize; y++) {
            for (int x = 0; x < absoluteSize; x++) {
                String unitString = getUnitString(y, x);
                fieldBuilder.append(unitString);
            }
            fieldBuilder.append("\n");
        }
        return fieldBuilder.toString();
    }

    private String getUnitString(int y, int x) {
        String unitString;
        if ((gameField[y][x].status & CAR.getStatus()) != 0) {
            unitString = "C";
        } else if ((gameField[y][x].status & MOPED.getStatus()) != 0) {
            unitString = "M";
        } else if ((gameField[y][x].status & TRUCK.getStatus()) != 0) {
            unitString = "T";
        } else if ((gameField[y][x].status & FINISH) != 0) {
            unitString = "▒";
        } else if ((gameField[y][x].status & START) != 0) {
            unitString = "║";
        } else if ((gameField[y][x].status & ABYSS) != 0) {
            unitString = "♦";
        } else if ((gameField[y][x].status & ROAD) != 0) {
            unitString = "█";
        } else {
            unitString = "";
        }

        return unitString;
    }
}
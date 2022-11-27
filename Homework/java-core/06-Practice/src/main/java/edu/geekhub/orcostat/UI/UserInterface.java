package edu.geekhub.orcostat.UI;

import edu.geekhub.orcostat.DBmigration.TrivialMigration;
import edu.geekhub.orcostat.controller.MilitaryLossController;
import edu.geekhub.orcostat.controller.StatisticTableController;
import edu.geekhub.orcostat.model.collections.OrcEquipage;
import edu.geekhub.orcostat.model.entity.*;
import edu.geekhub.orcostat.model.request.Request;
import edu.geekhub.orcostat.model.request.Response;
import edu.geekhub.orcostat.model.request.ResponseStatus;

import java.time.LocalDate;
import java.util.Scanner;

import static edu.geekhub.orcostat.utils.MessagesColors.*;
import static java.lang.System.*;

public class UserInterface {
    private final MilitaryLossController militaryController;
    private final StatisticTableController tableController;
    private final TrivialMigration trivialMigration;
    private final Scanner scanner;
    private boolean isRunning;

    public UserInterface(MilitaryLossController militaryController,
                         StatisticTableController tableController,
                         TrivialMigration trivialMigration,
                         Scanner scanner) {
        this.militaryController = militaryController;
        this.tableController = tableController;
        this.trivialMigration = trivialMigration;
        this.scanner = scanner;
    }

    public void run() {
        trivialMigration.initMigration();
        isRunning = true;
        drawStartUpMenu();
        while (isRunning) {
            String userChoice = scanner.nextLine().trim();
            if (userChoice.isBlank()) continue;
            switch (userChoice) {
                case "1" -> {
                    printOrcPrompt();
                    Response response = militaryController.addMilitary(new Request(new Orc()));
                    printResponse(response);
                }
                case "2" -> {
                    printTankPrompt();
                    int equipageCount = scanner.nextInt();
                    try {
                        Tank tank = new Tank(OrcEquipage.generateEquipage(equipageCount));
                        Response response = militaryController.addMilitary(new Request(tank));
                        printResponse(response);
                    } catch (IllegalArgumentException e) {
                        printExceptionMessage(e);
                    }
                }
                case "3" -> {
                    printDronePrompt();
                    Response response = militaryController.addMilitary(new Request(new Drone()));
                    printResponse(response);
                }
                case "4" -> {
                    printMissilePrompt();
                    Response response = militaryController.addMilitary(new Request(new Missile()));
                    printResponse(response);
                }
                case "5" -> {
                    printMoneyPrompt();
                    Response response = getMoneyStatisticResponse();
                    printResponse(response);
                }
                case "6" -> {
                    printStatisticPrompt();
                    Response response = getGeneralStatisticResponse();
                    printResponse(response);
                }
                case "7" -> {
                    isRunning = false;
                }
                default -> printCannotResolveInput(userChoice);
            }
            drawStartUpMenu();
        }
    }

    private Response getGeneralStatisticResponse() {
        Response response;
        try {
            String period = scanner.nextLine();
            if (period.equals("1")) {
                response = tableController.renderGeneralStatistic();
            } else if (period.equals("2")) {
                response = tableController.renderGeneralStatisticWithDate(LocalDate.now());
            } else {
                response = Response.fail("No such statistic type provided");
            }
        } catch (IllegalArgumentException e) {
            response = Response.fail(e.getMessage());
        }
        return response;
    }

    private Response getMoneyStatisticResponse() {
        Response response;
        try {
            String period = scanner.nextLine();
            if (period.equals("1")) {
                response = tableController.renderMoneyTable();
            } else if (period.equals("2")) {
                response = tableController.renderMoneyTableWithDate(LocalDate.now());
            } else {
                response = Response.fail("No such period type provided");
            }
        } catch (IllegalArgumentException e) {
            response = Response.fail(e.getMessage());
        }
        return response;
    }

    public void printResponse(Response response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            if (response.getData() instanceof Table table) {
                out.printf(
                        ANSI_CYAN +
                        "%n%s" +
                        ANSI_RESET +
                        "%n",
                        table
                );
            } else {
                out.printf(
                        ANSI_GREEN +
                        "%n%s" +
                        ANSI_RESET +
                        "%n",
                        response.getData()
                );
            }
        } else {
            out.printf(
                    ANSI_RED +
                    "%n%s" +
                    ANSI_RESET +
                    "%n",
                    response.getData()
            );
        }
    }

    public void printExceptionMessage(Exception e) {
        out.printf(
                ANSI_RED +
                "%n%s" +
                ANSI_RESET +
                "%n",
                e.getMessage()
        );
    }

    public void drawStartUpMenu() {
        StringBuilder startMenuBuilder = new StringBuilder()
                .append("\nIf you want to add orc enter 1\n")
                .append("If you want to add tank enter 2\n")
                .append("If you want to add drone enter 3\n")
                .append("If you want to add missile enter 4\n")
                .append("If you want to see money damage enter 5\n")
                .append("If you want to see statistic damage enter 6\n")
                .append("If you want to end session enter 7");

        out.println(startMenuBuilder);
    }

    public void printOrcPrompt() {
        StringBuilder orcPromptBuilder = new StringBuilder()
                .append("\nYou have chosen to add an orc");
        out.println(orcPromptBuilder);
    }

    public void printTankPrompt() {
        StringBuilder tankPromptBuilder = new StringBuilder()
                .append("\nYou have chosen to add a tank\n")
                .append("Enter single integer from 0 to 6, tank equipage");
        out.println(tankPromptBuilder);
    }

    public void printDronePrompt() {
        StringBuilder dronePromptBuilder = new StringBuilder()
                .append("\nYou have chosen to add a drone");
        out.println(dronePromptBuilder);
    }

    public void printMissilePrompt() {
        StringBuilder missilePromptBuilder = new StringBuilder()
                .append("\nYou have chosen to add a missile");
        out.println(missilePromptBuilder);
    }

    public void printMoneyPrompt() {
        StringBuilder printMoneyBuilder = new StringBuilder()
                .append("\nYou have chosen to print money damage\n")
                .append("If you want to see total statistic enter 1\n")
                .append("If you want to see daily statistics enter 2");
        out.println(printMoneyBuilder);
    }

    public void printStatisticPrompt() {
        StringBuilder printStatisticBuilder = new StringBuilder()
                .append("\nYou have chosen to print statistic\n")
                .append("If you want to see total statistic enter 1\n")
                .append("If you want to see daily statistics enter 2");
        out.println(printStatisticBuilder);
    }

    public void printCannotResolveInput(Object input) {
        out.printf(
                ANSI_RED +
                "%nInput [%s] doesn't supports" +
                ANSI_RESET +
                "%n"
                , input);
    }
}

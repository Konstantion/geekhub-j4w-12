package edu.geekhub.orcostat.controller;

import edu.geekhub.orcostat.model.entity.Table;
import edu.geekhub.orcostat.model.request.Response;
import edu.geekhub.orcostat.service.StatisticTableService;

import java.time.LocalDate;

public class StatisticTableController {
    private final StatisticTableService statisticTableService;

    public StatisticTableController(StatisticTableService statisticTableService) {
        this.statisticTableService = statisticTableService;
    }

    public Response renderOrcTable() {
        try {
            Table table = new Table(statisticTableService.getTroopsTable());
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }

    }

    public Response renderTankTable() {
        try {
            Table table = new Table(statisticTableService.getTanksTable());
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    public Response renderDroneTable() {
        try {
            Table table = new Table(statisticTableService.getDronesTable());
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    public Response renderMissileTable() {
        try {
            Table table = new Table(statisticTableService.getMissileTable());
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    public Response renderMoneyTable() {
        try {
            Table table = new Table(statisticTableService.getMoneyDamageTable());
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    public Response renderGeneralStatistic() {
        try {
            StringBuilder generalStatisticBuilder = new StringBuilder()
                    .append(renderOrcTable().getData())
                    .append(System.lineSeparator())
                    .append(renderTankTable().getData())
                    .append(System.lineSeparator())
                    .append(renderDroneTable().getData())
                    .append(System.lineSeparator())
                    .append(renderMissileTable().getData());
            return Response.ok(new Table(
                            generalStatisticBuilder.toString()
                    )
            );
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }


    }

    public Response renderOrcTableWithDate(LocalDate date) {
        try {
            Table table = new Table(statisticTableService.getTroopsTable(date));
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }

    }

    public Response renderTankTableWithDate(LocalDate date) {
        try {
            Table table = new Table(statisticTableService.getTanksTable(date));
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    public Response renderDroneTableWithDate(LocalDate date) {
        try {
            Table table = new Table(statisticTableService.getDronesTable(date));
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    public Response renderMissileTableWithDate(LocalDate date) {
        try {
            Table table = new Table(statisticTableService.getMissileTable(date));
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    public Response renderMoneyTableWithDate(LocalDate date) {
        try {
            Table table = new Table(statisticTableService.getMoneyDamageTable(date));
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    public Response renderGeneralStatisticWithDate(LocalDate date) {
        try {
            StringBuilder generalStatisticBuilder = new StringBuilder()
                    .append(renderOrcTableWithDate(date).getData())
                    .append(System.lineSeparator())
                    .append(renderTankTableWithDate(date).getData())
                    .append(System.lineSeparator())
                    .append(renderDroneTableWithDate(date).getData())
                    .append(System.lineSeparator())
                    .append(renderMissileTableWithDate(date).getData());

            return Response.ok(new Table(
                            generalStatisticBuilder.toString()
                    )
            );
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }


    }
}

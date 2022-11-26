package edu.geekhub.orcostat.controller;

import edu.geekhub.orcostat.model.request.Response;
import edu.geekhub.orcostat.service.StatisticTableService;

public class StatisticTableController {
    private final StatisticTableService statisticTableService;

    public StatisticTableController(StatisticTableService statisticTableService) {
        this.statisticTableService = statisticTableService;
    }

    public Response renderOrcTable() {
        try {
            String table = statisticTableService.getTroopsTable();
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }

    }

    public Response renderTankTable() {
        try {
            String table = statisticTableService.getTanksTable();
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    public Response renderDroneTable() {
        try {
            String table = statisticTableService.getDronesTable();
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }

    public Response renderMissileTable() {
        try {
            String table = statisticTableService.getMissileTable();
            return Response.ok(table);
        } catch (Exception e) {
            return Response.fail(e.getMessage());
        }
    }
}

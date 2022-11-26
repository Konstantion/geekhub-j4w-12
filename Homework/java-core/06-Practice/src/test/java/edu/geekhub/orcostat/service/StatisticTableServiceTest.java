package edu.geekhub.orcostat.service;

import edu.geekhub.orcostat.DBmigration.TrivialMigration;
import edu.geekhub.orcostat.beans.DatabaseBean;
import edu.geekhub.orcostat.repository.TrivialDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatisticTableServiceTest {
        MilitaryLossService militaryLossService;
        StatisticTableService tableService;
        TrivialDatabase database;
        TrivialMigration migration;
        @BeforeEach
        void setUp() {
                database = DatabaseBean.INSTANCE.getPrototype();
                militaryLossService = new MilitaryLossService(database);
                tableService = new StatisticTableService(militaryLossService);
                migration = new TrivialMigration(database);
                migration.initMigrationForTableTest();
        }



        @Test
        void draw_money_table() {
                System.out.println(tableService.getMoneyDamageTable());
        }

        @Test
        void draw_troops_table() {
                System.out.println(tableService.getTroopsTable());
        }

        @Test
        void draw_tanks_table() {
                System.out.println(tableService.getTanksTable());
        }

        @Test
        void draw_drones_table() {
                System.out.println(tableService.getDronesTable());
        }

        @Test
        void draw_missile_table() {
                System.out.println(tableService.getMissileTable());
        }
}
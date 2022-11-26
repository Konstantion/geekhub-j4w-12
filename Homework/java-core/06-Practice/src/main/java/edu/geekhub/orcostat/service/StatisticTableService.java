package edu.geekhub.orcostat.service;

import edu.geekhub.orcostat.model.collections.LocalDateLongPair;
import edu.geekhub.orcostat.model.collections.TrivialLocalDateHashMap;
import edu.geekhub.orcostat.model.entity.MilitaryLoss;

import java.time.LocalDate;

public class StatisticTableService {
    private final MilitaryLossService militaryLossService;

    public StatisticTableService(MilitaryLossService militaryLossService) {
        this.militaryLossService = militaryLossService;
    }

    public String getMoneyDamageTable() {
        MilitaryLoss[] militaryLosses = militaryLossService.getDatabase().getAllMilitaryLoss();

        TrivialLocalDateHashMap hashMap = new TrivialLocalDateHashMap();
        for (MilitaryLoss militaryLoss : militaryLosses) {
            hashMap.checkAndAdd(new LocalDateLongPair(
                    militaryLoss.getDate(),
                    militaryLoss.getPrice()
            ));
        }

        return buildTable(hashMap, "MONEY");
    }

    public String getTroopsTable() {
        MilitaryLoss[] militaryLosses = militaryLossService.getDatabase().getAllOrcs();

        TrivialLocalDateHashMap hashMap = new TrivialLocalDateHashMap();
        for (MilitaryLoss militaryLoss : militaryLosses) {
            hashMap.checkAndAdd(new LocalDateLongPair(
                    militaryLoss.getDate(),
                    1
            ));
        }
        return buildTable(hashMap, "TROOPS");
    }

    public String getTanksTable() {
        MilitaryLoss[] militaryLosses = militaryLossService.getDatabase().getAllTanks();

        TrivialLocalDateHashMap hashMap = new TrivialLocalDateHashMap();
        for (MilitaryLoss militaryLoss : militaryLosses) {
            hashMap.checkAndAdd(new LocalDateLongPair(
                    militaryLoss.getDate(),
                    1
            ));
        }
        return buildTable(hashMap, "TANKS");
    }

    public String getDronesTable() {
        MilitaryLoss[] militaryLosses = militaryLossService.getDatabase().getAllDrones();

        TrivialLocalDateHashMap hashMap = new TrivialLocalDateHashMap();
        for (MilitaryLoss militaryLoss : militaryLosses) {
            hashMap.checkAndAdd(new LocalDateLongPair(
                    militaryLoss.getDate(),
                    1
            ));
        }
        return buildTable(hashMap, "DRONES");
    }

    public String getMissileTable() {
        MilitaryLoss[] militaryLosses = militaryLossService.getDatabase().getAllMissiles();

        TrivialLocalDateHashMap hashMap = new TrivialLocalDateHashMap();
        for (MilitaryLoss militaryLoss : militaryLosses) {
            hashMap.checkAndAdd(new LocalDateLongPair(
                    militaryLoss.getDate(),
                    1
            ));
        }
        return buildTable(hashMap, "MISSILES");
    }

    public String getMoneyDamageTable(LocalDate date) {
        MilitaryLoss[] militaryLosses = militaryLossService.getDatabase().getAllMilitaryLossWithDate(date);

        TrivialLocalDateHashMap hashMap = new TrivialLocalDateHashMap();
        for (MilitaryLoss militaryLoss : militaryLosses) {
            hashMap.checkAndAdd(new LocalDateLongPair(
                    militaryLoss.getDate(),
                    militaryLoss.getPrice()
            ));
        }

        return buildTable(hashMap, "MONEY");
    }

    public String getTroopsTable(LocalDate date) {
        MilitaryLoss[] militaryLosses = militaryLossService.getDatabase().getAllOrcsWithLocalDate(date);

        TrivialLocalDateHashMap hashMap = new TrivialLocalDateHashMap();
        for (MilitaryLoss militaryLoss : militaryLosses) {
            hashMap.checkAndAdd(new LocalDateLongPair(
                    militaryLoss.getDate(),
                    1
            ));
        }
        return buildTable(hashMap, "TROOPS");
    }

    public String getTanksTable(LocalDate date) {
        MilitaryLoss[] militaryLosses = militaryLossService.getDatabase().getAllTanksWithLocalDate(date);

        TrivialLocalDateHashMap hashMap = new TrivialLocalDateHashMap();
        for (MilitaryLoss militaryLoss : militaryLosses) {
            hashMap.checkAndAdd(new LocalDateLongPair(
                    militaryLoss.getDate(),
                    1
            ));
        }
        return buildTable(hashMap, "TANKS");
    }

    public String getDronesTable(LocalDate date) {
        MilitaryLoss[] militaryLosses = militaryLossService.getDatabase().getAllDronesWithLocalDate(date);

        TrivialLocalDateHashMap hashMap = new TrivialLocalDateHashMap();
        for (MilitaryLoss militaryLoss : militaryLosses) {
            hashMap.checkAndAdd(new LocalDateLongPair(
                    militaryLoss.getDate(),
                    1
            ));
        }
        return buildTable(hashMap, "DRONES");
    }

    public String getMissileTable(LocalDate date) {
        MilitaryLoss[] militaryLosses = militaryLossService.getDatabase().getAllMissilesWithLocalDate(date);

        TrivialLocalDateHashMap hashMap = new TrivialLocalDateHashMap();
        for (MilitaryLoss militaryLoss : militaryLosses) {
            hashMap.checkAndAdd(new LocalDateLongPair(
                    militaryLoss.getDate(),
                    1
            ));
        }
        return buildTable(hashMap, "MISSILES");
    }

    private String buildTable(TrivialLocalDateHashMap hashMap,
                              String parameter) {
        StringBuilder table = new StringBuilder();
        table.append(
                String.format(
                        "%s%n" +
                        "|%-10S|%-15S|%-100S|%n" +
                        "%s%n",
                        "-".repeat(129),
                        "PERIOD",
                        parameter + " COUNT",
                        parameter + " CHART",
                        "-".repeat(129)
                )
        );
        LocalDateLongPair[] rowsData = hashMap.extractArray();
        long maxDailyParameterValue = getMaxDailyValueLoss(rowsData);
        for (LocalDateLongPair row : rowsData) {
            table.append(
                    String.format(
                            "|%-10s|%15s|%-100s|%n",
                            row.getDate(),
                            row.getValue(),
                            "#".repeat((int) (((double) row.getValue() / maxDailyParameterValue) * 100))
                    )
            );
        }
        table.append("-".repeat(129));
        table.append(System.lineSeparator());

        return table.toString();
    }

    private long getMaxDailyValueLoss(LocalDateLongPair[] rawPairs) {
        long max = 0;
        for (LocalDateLongPair pair : rawPairs) {
            if (pair.getValue() > max) {
                max = pair.getValue();
            }
        }
        return max;
    }

}

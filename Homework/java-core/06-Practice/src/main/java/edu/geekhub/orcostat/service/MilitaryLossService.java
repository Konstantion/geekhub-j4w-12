package edu.geekhub.orcostat.service;

import edu.geekhub.orcostat.model.entity.*;
import edu.geekhub.orcostat.repository.TrivialDatabase;

import java.time.LocalDate;

public class MilitaryLossService {
    private final TrivialDatabase database;

    public MilitaryLossService(TrivialDatabase database) {
        this.database = database;
    }

    public void addNegativelyAliveOrc(Orc orc) {
        database.save(orc);
    }

    public void addDestroyedTank(Tank tank) {
        database.save(tank);

    }

    public void addDestroyedDrone(Drone drone) {
        database.save(drone);
    }

    public void addDestroyedMissile(Missile missile) {
        database.save(missile);

    }

    public int getNegativelyAliveOrcCount() {
        return database.getAllOrcs().length;
    }

    public int getDestroyedTanksCount() {
        return database.getAllTanks().length;
    }

    public int getDestroyedDronesCount() {
        return database.getAllDrones().length;
    }

    public int getDestroyedMissilesCount() {
        return database.getAllMissiles().length;
    }

    public int getNegativelyAliveOrcCountWithDate(LocalDate date) {
        return database.getAllOrcsWithLocalDate(date).length;
    }

    public int getDestroyedTanksCountWithDate(LocalDate date) {
        return database.getAllTanksWithLocalDate(date).length;
    }

    public int getDestroyedDronesCountWithDate(LocalDate date) {
        return database.getAllDronesWithLocalDate(date).length;
    }

    public int getDestroyedMissilesCountWithDate(LocalDate date) {
        return database.getAllMissilesWithLocalDate(date).length;
    }

    public long getLosesInDollars() {
        MilitaryLoss[] militaryLosses = database.getAllMilitaryLoss();
        long militaryLossesInDollars = 0;

        for (MilitaryLoss militaryLoss : militaryLosses) {
            militaryLossesInDollars += militaryLoss.getPrice();
        }

        return militaryLossesInDollars;
    }

    public long getLosesInDollarsWithDate(LocalDate date) {
        MilitaryLoss[] militaryLosses = database.getAllMilitaryLossWithDate(date);
        long militaryLossesInDollars = 0;

        for (MilitaryLoss militaryLoss : militaryLosses) {
            militaryLossesInDollars += militaryLoss.getPrice();
        }

        return militaryLossesInDollars;
    }

    public TrivialDatabase getDatabase() {
        return database;
    }
}

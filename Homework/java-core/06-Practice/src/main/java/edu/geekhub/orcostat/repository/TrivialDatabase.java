package edu.geekhub.orcostat.repository;

import edu.geekhub.orcostat.model.collections.TrivialDroneCollection;
import edu.geekhub.orcostat.model.collections.TrivialMissileCollection;
import edu.geekhub.orcostat.model.collections.TrivialOrcCollection;
import edu.geekhub.orcostat.model.collections.TrivialTankCollection;
import edu.geekhub.orcostat.model.entity.*;
import edu.geekhub.orcostat.model.utils.TrivialArrayUtil;

import java.time.LocalDate;
import java.util.Arrays;

public class TrivialDatabase {
    private final TrivialTankCollection tankTable;
    private final TrivialOrcCollection orcTable;
    private final TrivialDroneCollection droneTable;
    private final TrivialMissileCollection missileTable;


    public TrivialDatabase(TrivialTankCollection tankTable,
                           TrivialOrcCollection orcTable,
                           TrivialDroneCollection droneTable,
                           TrivialMissileCollection missileTable) {
        this.tankTable = tankTable;
        this.orcTable = orcTable;
        this.droneTable = droneTable;
        this.missileTable = missileTable;
    }

    public void save(Tank tank) {
        Orc[] equipage = tank.getEquipage().getData();
        for (Orc orc : equipage) {
            save(orc);
        }
        tankTable.add(tank);
    }

    public void save(Orc orc) {
        orcTable.add(orc);
    }

    public void save(Drone drone) {
        droneTable.add(drone);
    }

    public void save(Missile missile) {
        missileTable.add(missile);
    }

    public Orc[] getAllOrcs() {
        return orcTable.getData();
    }

    public Tank[] getAllTanks() {
        return tankTable.getData();
    }

    public Drone[] getAllDrones() {
        return droneTable.getData();
    }

    public Missile[] getAllMissiles() {
        return missileTable.getData();
    }

    public Orc[] getAllOrcsWithLocalDate(LocalDate date) {
        MilitaryLoss[] filteredArray = TrivialArrayUtil.filterMilitaryLossByLocalDate(
                getAllOrcs(),
                date);
        return Arrays.copyOf(filteredArray, filteredArray.length, Orc[].class);
    }

    public Tank[] getAllTanksWithLocalDate(LocalDate date) {
        MilitaryLoss[] filteredArray = TrivialArrayUtil.filterMilitaryLossByLocalDate(
                getAllTanks(),
                date);
        return Arrays.copyOf(filteredArray, filteredArray.length, Tank[].class);
    }

    public Drone[] getAllDronesWithLocalDate(LocalDate date) {
        MilitaryLoss[] filteredArray = TrivialArrayUtil.filterMilitaryLossByLocalDate(
                getAllDrones(),
                date
        );
        return Arrays.copyOf(filteredArray, filteredArray.length, Drone[].class);
    }

    public Missile[] getAllMissilesWithLocalDate(LocalDate date) {
        MilitaryLoss[] filteredArray = TrivialArrayUtil.filterMilitaryLossByLocalDate(
                getAllMissiles(),
                date
        );
        return Arrays.copyOf(filteredArray, filteredArray.length, Missile[].class);
    }
}

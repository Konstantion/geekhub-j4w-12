package edu.geekhub.orcostat.DBmigration;

import edu.geekhub.orcostat.model.collections.OrcEquipage;
import edu.geekhub.orcostat.model.entity.Drone;
import edu.geekhub.orcostat.model.entity.Missile;
import edu.geekhub.orcostat.model.entity.Orc;
import edu.geekhub.orcostat.model.entity.Tank;
import edu.geekhub.orcostat.repository.TrivialDatabase;

import java.security.SecureRandom;
import java.time.LocalDate;

public class TrivialMigration {

    private final SecureRandom random;
    private final TrivialDatabase database;

    public TrivialMigration(TrivialDatabase database) {
        random = new SecureRandom();
        this.database = database;
    }

    private void initMilitaryLoss(LocalDate date) {
        int maxIndex =  random.nextInt(100,900);
        for(int i = 0; i < maxIndex; i++) {
            database.save(new Orc(random.nextInt(10_000, 30_000), date));
            if(i % 15 ==0) {
                new Tank(OrcEquipage.generateRandomEquipage());
            }
            if(i % 25 == 0) {
                database.save(new Tank(random.nextInt(2_000_000,8_000_000), date));
            }
            if(i % 35 == 0) {
                database.save(new Drone(random.nextInt(100_000,1_000_000), date));
            }
            if(i % 45 == 0) {
                database.save(new Missile(random.nextInt(10_000_000,40_000_000), date));
            }
        }
    }

    public void initMigration() {
        initMilitaryLoss(LocalDate.now().minusDays(1));
        initMilitaryLoss(LocalDate.now().minusDays(2));
        initMilitaryLoss(LocalDate.now().minusDays(3));
        initMilitaryLoss(LocalDate.now().minusDays(4));
        initMilitaryLoss(LocalDate.now().minusDays(5));
        initMilitaryLoss(LocalDate.now().minusDays(6));
        initMilitaryLoss(LocalDate.now().minusDays(7));
        initMilitaryLoss(LocalDate.now().minusDays(8));
        initMilitaryLoss(LocalDate.now().minusDays(9));
    }

    public void initMigrationForTableTest() {
        for (int i = 0; i < 5; i++) {
            database.save(new Orc(LocalDate.now()));
        }
        database.save(new Tank(LocalDate.now()));
        database.save(new Tank(OrcEquipage.generateRandomEquipage()));
        database.save(new Drone(LocalDate.now()));
        database.save(new Missile(LocalDate.now()));
    }
}

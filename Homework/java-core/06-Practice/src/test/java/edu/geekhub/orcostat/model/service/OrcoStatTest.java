package edu.geekhub.orcostat.model.service;

import edu.geekhub.orcostat.beans.DatabaseBean;
import edu.geekhub.orcostat.model.entity.Orc;
import edu.geekhub.orcostat.model.entity.Tank;
import edu.geekhub.orcostat.model.collections.TrivialOrcCollection;
import edu.geekhub.orcostat.service.MilitaryLossService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrcoStatTest {

    private MilitaryLossService orcoStatService;

    @BeforeEach
    void setUp() {
        orcoStatService = new MilitaryLossService(DatabaseBean.INSTANCE.getPrototype());
    }

    @Test
    void can_count_negatively_alive_orc() {
        int count = orcoStatService.getNegativelyAliveOrcCount();

        assertEquals(0, count);
    }

    @Test
    void can_add_negatively_alive_orc() {
        orcoStatService.addNegativelyAliveOrc(new Orc());

        int count = orcoStatService.getNegativelyAliveOrcCount();

        assertEquals(1, count);
    }

    @Test
    void can_add_multiple_negatively_alive_orcs() {
        orcoStatService.addNegativelyAliveOrc(new Orc());
        orcoStatService.addNegativelyAliveOrc(new Orc());

        int count = orcoStatService.getNegativelyAliveOrcCount();

        assertEquals(2, count);
    }

    @Test
    void can_count_destroyed_tanks() {
        int count = orcoStatService.getDestroyedTanksCount();

        assertEquals(0, count);
    }

    @Test
    void can_add_destroyed_tank_without_orc() {
        orcoStatService.addDestroyedTank(new Tank());

        int count = orcoStatService.getDestroyedTanksCount();

        assertEquals(1, count);
    }

    @Test
    void can_add_destroyed_tank_with_orcs() {
        TrivialOrcCollection equipage = new TrivialOrcCollection();
        equipage.add(new Orc());
        equipage.add(new Orc());
        equipage.add(new Orc());
        equipage.add(new Orc());
        orcoStatService.addDestroyedTank(new Tank(equipage));

        int tanksCount = orcoStatService.getDestroyedTanksCount();
        int orcCount = orcoStatService.getNegativelyAliveOrcCount();

        assertEquals(1, tanksCount);
        assertEquals(4, orcCount);
    }

    @Test
    void can_count_orcs_loses_in_dollars() {
        long damage = orcoStatService.getLosesInDollars();

        assertEquals(0, damage);
    }

    @Test
    void can_sum_orc_loses_in_dollars() {
        orcoStatService.addNegativelyAliveOrc(new Orc());

        long damage = orcoStatService.getLosesInDollars();

        assertEquals(10_000, damage);
    }

    @Test
    void can_sum_tank_loses_in_dollars() {
        orcoStatService.addDestroyedTank(new Tank());

        long losesInDollars = orcoStatService.getLosesInDollars();

        assertEquals(3_000_000, losesInDollars);
    }

    @Test
    void can_sum_tank_with_orc_as_equipage_loses_cost_in_dollars() {
        TrivialOrcCollection equipage = new TrivialOrcCollection();
        equipage.add(new Orc());
        orcoStatService.addDestroyedTank(new Tank(equipage));

        long losesInDollars = orcoStatService.getLosesInDollars();

        assertEquals(3_010_000, losesInDollars);
    }

    @Test
    void can_sum_tank_with_orc_as_equipage_and_lost_orc_loses_cost_in_dollars() {
        TrivialOrcCollection equipage = new TrivialOrcCollection();
        equipage.add(new Orc());
        orcoStatService.addDestroyedTank(new Tank(equipage));
        orcoStatService.addNegativelyAliveOrc(new Orc());

        long losesInDollars = orcoStatService.getLosesInDollars();

        assertEquals(3_020_000, losesInDollars);
    }
}

package edu.geekhub.orcostat.controller;

import edu.geekhub.orcostat.beans.DatabaseBean;
import edu.geekhub.orcostat.beans.MilitaryLossServiceBean;
import edu.geekhub.orcostat.model.entity.Orc;
import edu.geekhub.orcostat.model.request.Request;
import edu.geekhub.orcostat.repository.TrivialDatabase;
import edu.geekhub.orcostat.service.MilitaryLossService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MilitaryLossControllerTest {

    private MilitaryLossController controller;
    private MilitaryLossService service;

    @BeforeEach
    void setUp() {
        service = MilitaryLossServiceBean.INSTANCE.getPrototype();
        controller = new MilitaryLossController(service);
    }

    @Test
    void add_orc() {
        Orc orc = new Orc();
        Request request = new Request(orc);
        controller.addMilitary(request);

        int actualOrcsCount = service.getNegativelyAliveOrcCount();
        int expectedOrcsCount = 1;

        assertEquals(expectedOrcsCount, actualOrcsCount);
    }
}
package edu.geekhub.orcostat.controller;

import edu.geekhub.orcostat.beans.DatabaseBean;
import edu.geekhub.orcostat.beans.MilitaryLossServiceBean;
import edu.geekhub.orcostat.model.collections.TrivialOrcCollection;
import edu.geekhub.orcostat.model.entity.Orc;
import edu.geekhub.orcostat.model.entity.Tank;
import edu.geekhub.orcostat.model.request.Request;
import edu.geekhub.orcostat.model.request.Response;
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
        Response response = controller.addMilitary(request);

        int actualOrcsCount = service.getNegativelyAliveOrcCount();
        int expectedOrcsCount = 1;
        String actualResponse = response.toString();
        String expectedResponse = Response.ok(String.format(
                        "Successfully saved [%s]",
                        orc
                ))
                .toString();

        assertEquals(expectedOrcsCount, actualOrcsCount);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void add_orc_null() {
        Orc orc = null;
        Request request = new Request(orc);

        Response response = controller.addMilitary(request);
        int actualOrcsCount = service.getNegativelyAliveOrcCount();
        int expectedOrcsCount = 0;
        String actualResponse = response.toString();
        String expectedResponse = Response.fail(String.format(
                        "Cannot find Military unit [%s]",
                        orc
                ))
                .toString();

        assertEquals(expectedOrcsCount, actualOrcsCount);
        assertEquals(expectedResponse, actualResponse);
    }
}
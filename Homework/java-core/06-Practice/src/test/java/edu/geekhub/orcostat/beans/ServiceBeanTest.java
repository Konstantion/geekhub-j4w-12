package edu.geekhub.orcostat.beans;

import edu.geekhub.orcostat.repository.TrivialDatabase;
import edu.geekhub.orcostat.service.MilitaryLossService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceBeanTest {

    @Test
    void singletons_have_one_database() {
        MilitaryLossService service =  MilitaryLossServiceBean.INSTANCE.getSingleton();
        TrivialDatabase database = DatabaseBean.INSTANCE.getSingleton();

        assertEquals(database, service.getDatabase());
    }

}
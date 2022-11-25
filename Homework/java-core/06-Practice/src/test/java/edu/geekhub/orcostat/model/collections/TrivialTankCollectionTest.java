package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.entity.Orc;
import edu.geekhub.orcostat.model.entity.Tank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrivialTankCollectionTest {
    @Test
    void can_create() {
        new TrivialTankCollection();
    }

    @Test
    void can_add_element() {
        TrivialTankCollection tank = new TrivialTankCollection();

        tank.add(new Tank());

        assertEquals(1, tank.count());
    }

    @Test
    void can_add_two_elements() {
        TrivialTankCollection tank = new TrivialTankCollection();

        tank.add(new Tank());
        tank.add(new Tank());

        assertEquals(2, tank.count());
    }

    @Test
    void cannot_add_undefined_element() {
        TrivialTankCollection tank = new TrivialTankCollection();

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> tank.add(null)
        );
        assertEquals("Object cannot be null", thrown.getMessage());
    }

    @Test
    void trivial_collection_extension_automatic() {
        TrivialTankCollection tank = new TrivialTankCollection();
        int actualCountOfElements = 2_000;

        for (int i = 0; i < actualCountOfElements; i++) {
            tank.add(new Tank());
        }


        assertEquals(actualCountOfElements, tank.count());
    }

    @Test
    void trivial_Tank_collection_cant_save_another_objects() {
        TrivialTankCollection tanks = new TrivialTankCollection();
        Orc orc = new Orc();

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> tanks.add(orc)
        );

        assertEquals("Object must be Tank", thrown.getMessage());
    }

    @Test
    void trivial_Tank_collection_can_return_data() {
        TrivialTankCollection tank = new TrivialTankCollection();
        Tank first_Tank = new Tank();
        Tank second_Tank = new Tank();

        tank.add(first_Tank);
        tank.add(second_Tank);
        Tank[] actualTank = new Tank[]{first_Tank, second_Tank};

        assertArrayEquals(actualTank, tank.getData());
    }
}
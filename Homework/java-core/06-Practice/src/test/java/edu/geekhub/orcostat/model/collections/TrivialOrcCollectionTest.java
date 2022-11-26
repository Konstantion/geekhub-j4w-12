package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.entity.Orc;
import edu.geekhub.orcostat.model.entity.Tank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrivialOrcCollectionTest {
    @Test
    void can_create() {
        new TrivialOrcCollection();
    }

    @Test
    void can_add_element() {
        TrivialOrcCollection orcs = new TrivialOrcCollection();

        orcs.add(new Orc());

        assertEquals(1, orcs.count());
    }

    @Test
    void can_add_two_elements() {
        TrivialOrcCollection orcs = new TrivialOrcCollection();

        orcs.add(new Orc());
        orcs.add(new Orc());

        assertEquals(2, orcs.count());
    }

    @Test
    void cannot_add_undefined_element() {
        TrivialOrcCollection orcs = new TrivialOrcCollection();

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> orcs.add(null)
        );
        assertEquals("Object cannot be null", thrown.getMessage());
    }

    @Test
    void trivial_collection_extension_automatic() {
        TrivialOrcCollection orcs = new TrivialOrcCollection();
        int actualCountOfElements = 2_000;

        for (int i = 0; i < actualCountOfElements; i++) {
            orcs.add(new Orc());
        }


        assertEquals(actualCountOfElements, orcs.count());
    }

    @Test
    void trivial_orc_collection_cant_save_another_objects() {
        TrivialOrcCollection orcs = new TrivialOrcCollection();
        Tank tank = new Tank();

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> orcs.add(tank)
        );

        assertEquals("Object must be Orc", thrown.getMessage());
    }

    @Test
    void trivial_orc_collection_can_return_data() {
        TrivialOrcCollection orcs = new TrivialOrcCollection();
        Orc first_Orc = new Orc();
        Orc second_Orc = new Orc();

        orcs.add(first_Orc);
        orcs.add(second_Orc);
        Orc[] actualOrcs = new Orc[]{first_Orc, second_Orc};

        assertArrayEquals(actualOrcs, orcs.getData());
    }

    @Test
    void empty_array_when_zero_orcs() {
        TrivialOrcCollection orcs = new TrivialOrcCollection();

        Orc[] actualOrcs = orcs.getData();
        Orc[] expectedOrcs = new Orc[0];

        assertArrayEquals(expectedOrcs, actualOrcs);
    }
}
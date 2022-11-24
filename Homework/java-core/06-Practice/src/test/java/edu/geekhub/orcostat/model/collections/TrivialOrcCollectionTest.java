package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.Orc;
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
}
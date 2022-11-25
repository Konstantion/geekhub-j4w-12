package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.entity.Missile;
import edu.geekhub.orcostat.model.entity.Tank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrivialMissileCollectionTest {
    @Test
    void can_create() {
        new TrivialMissileCollection();
    }

    @Test
    void can_add_element() {
        TrivialMissileCollection missiles = new TrivialMissileCollection();

        missiles.add(new Missile());

        assertEquals(1, missiles.count());
    }

    @Test
    void can_add_two_elements() {
        TrivialMissileCollection missiles = new TrivialMissileCollection();

        missiles.add(new Missile());
        missiles.add(new Missile());

        assertEquals(2, missiles.count());
    }

    @Test
    void cannot_add_undefined_element() {
        TrivialMissileCollection missiles = new TrivialMissileCollection();

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> missiles.add(null)
        );
        assertEquals("Object cannot be null", thrown.getMessage());
    }

    @Test
    void trivial_collection_extension_automatic() {
        TrivialMissileCollection missiles = new TrivialMissileCollection();
        int actualCountOfElements = 2_000;

        for (int i = 0; i < actualCountOfElements; i++) {
            missiles.add(new Missile());
        }


        assertEquals(actualCountOfElements, missiles.count());
    }

    @Test
    void trivial_missile_collection_cant_save_another_objects() {
        TrivialMissileCollection missiles = new TrivialMissileCollection();
        Tank tank = new Tank();

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> missiles.add(tank)
        );

        assertEquals("Object must be Missile", thrown.getMessage());
    }

    @Test
    void trivial_missile_collection_can_return_data() {
        TrivialMissileCollection missiles = new TrivialMissileCollection();
        Missile first_Missile = new Missile();
        Missile second_Missile = new Missile();

        missiles.add(first_Missile);
        missiles.add(second_Missile);
        Missile[] actualMissiles = new Missile[]{first_Missile, second_Missile};

        assertArrayEquals(actualMissiles, missiles.getData());
    }
}
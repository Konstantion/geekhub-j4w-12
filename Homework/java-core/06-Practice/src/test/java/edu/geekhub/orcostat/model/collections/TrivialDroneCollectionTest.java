package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.entity.Drone;
import edu.geekhub.orcostat.model.entity.Tank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrivialDroneCollectionTest {
    @Test
    void can_create() {
        new TrivialDroneCollection();
    }

    @Test
    void can_add_element() {
        TrivialDroneCollection drones = new TrivialDroneCollection();

        drones.add(new Drone());

        assertEquals(1, drones.count());
    }

    @Test
    void can_add_two_elements() {
        TrivialDroneCollection drones = new TrivialDroneCollection();

        drones.add(new Drone());
        drones.add(new Drone());

        assertEquals(2, drones.count());
    }

    @Test
    void cannot_add_undefined_element() {
        TrivialDroneCollection drones = new TrivialDroneCollection();

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> drones.add(null)
        );
        assertEquals("Object cannot be null", thrown.getMessage());
    }

    @Test
    void trivial_collection_extension_automatic() {
        TrivialDroneCollection drones = new TrivialDroneCollection();
        int actualCountOfElements = 2_000;

        for (int i = 0; i < actualCountOfElements; i++) {
            drones.add(new Drone());
        }


        assertEquals(actualCountOfElements, drones.count());
    }

    @Test
    void trivial_drone_collection_cant_save_another_objects() {
        TrivialDroneCollection drones = new TrivialDroneCollection();
        Tank tank = new Tank();

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> drones.add(tank)
        );

        assertEquals("Object must be Drone", thrown.getMessage());
    }

    @Test
    void trivial_drone_collection_can_return_data() {
        TrivialDroneCollection drones = new TrivialDroneCollection();
        Drone first_Drone = new Drone();
        Drone second_Drone = new Drone();

        drones.add(first_Drone);
        drones.add(second_Drone);
        Drone[] actualDrones = new Drone[]{first_Drone, second_Drone};

        assertArrayEquals(actualDrones, drones.getData());
    }
}
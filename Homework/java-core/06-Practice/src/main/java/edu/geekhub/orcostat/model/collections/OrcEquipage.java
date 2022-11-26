package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.entity.Orc;

import java.security.SecureRandom;

public class OrcEquipage extends TrivialOrcCollection {
    private static final SecureRandom random = new SecureRandom();

    public static TrivialOrcCollection generateRandomEquipage() {
        TrivialOrcCollection equipage = new TrivialOrcCollection();
        for (int i = 0; i < random.nextInt(0, 7); i++) {
            equipage.add(new Orc());
        }
        return equipage;
    }

    public static TrivialOrcCollection generateEquipage(int bound) {
        if (bound < 0 || bound > 6) {
            throw new IllegalArgumentException("Equipage size must be between 0 and 6");
        }
        TrivialOrcCollection equipage = new TrivialOrcCollection();
        for (int i = 0; i < bound; i++) {
            equipage.add(new Orc());
        }
        return equipage;
    }
}

package edu.geekhub.orcostat.model.collections;

import edu.geekhub.orcostat.model.entity.Orc;

import java.security.SecureRandom;

public class OrcEquipage extends TrivialOrcCollection{
    private static final SecureRandom random = new SecureRandom();
    public static TrivialOrcCollection generateRandomEquipage() {
        TrivialOrcCollection equipage = new TrivialOrcCollection();
        for(int i = 0; i < random.nextInt(0,7); i++) {
            equipage.add(new Orc());
        }
        return equipage;
    }
}

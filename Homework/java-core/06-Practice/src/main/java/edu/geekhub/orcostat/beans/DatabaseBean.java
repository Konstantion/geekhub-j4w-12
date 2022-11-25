package edu.geekhub.orcostat.beans;

import edu.geekhub.orcostat.model.collections.TrivialDroneCollection;
import edu.geekhub.orcostat.model.collections.TrivialMissileCollection;
import edu.geekhub.orcostat.model.collections.TrivialOrcCollection;
import edu.geekhub.orcostat.model.collections.TrivialTankCollection;
import edu.geekhub.orcostat.repository.TrivialDatabase;

public enum DatabaseBean {
    INSTANCE;

    private final TrivialDatabase singleton = getPrototype();

    public TrivialDatabase getSingleton() {
        return singleton;
    }

    public TrivialDatabase getPrototype() {
        return new TrivialDatabase(
                new TrivialTankCollection(),
                new TrivialOrcCollection(),
                new TrivialDroneCollection(),
                new TrivialMissileCollection()
        );
    }
}

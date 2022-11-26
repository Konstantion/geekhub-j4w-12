package edu.geekhub.orcostat.beans;

import edu.geekhub.orcostat.controller.MilitaryLossController;

public enum MilitaryLossControllerBean {
    INSTANCE;

    private final MilitaryLossController singleton = new MilitaryLossController(
            MilitaryLossServiceBean.INSTANCE.getSingleton()
    );

    public MilitaryLossController getSingleton() {
        return singleton;
    }

    public MilitaryLossController getPrototype() {
        return new MilitaryLossController(MilitaryLossServiceBean.INSTANCE.getPrototype());
    }
}

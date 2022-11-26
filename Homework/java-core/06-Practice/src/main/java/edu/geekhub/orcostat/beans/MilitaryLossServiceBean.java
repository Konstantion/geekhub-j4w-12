package edu.geekhub.orcostat.beans;

import edu.geekhub.orcostat.service.MilitaryLossService;

public enum MilitaryLossServiceBean {
    INSTANCE;

    private final MilitaryLossService singleton = new MilitaryLossService(
            DatabaseBean.INSTANCE.getSingleton()
    );

    public MilitaryLossService getSingleton() {
        return singleton;
    }

    public MilitaryLossService getPrototype() {
        return new MilitaryLossService(DatabaseBean.INSTANCE.getPrototype());
    }
}

package edu.geekhub.orcostat.beans;

import edu.geekhub.orcostat.service.StatisticTableService;

public enum StatisticTableServiceBean {
    INSTANCE;

    private final StatisticTableService singleton = new StatisticTableService(
            MilitaryLossServiceBean.INSTANCE.getSingleton()
    );

    public StatisticTableService getSingleton() {
        return singleton;
    }

    public StatisticTableService getPrototype() {
        return new StatisticTableService( MilitaryLossServiceBean.INSTANCE.getPrototype());
    }
}

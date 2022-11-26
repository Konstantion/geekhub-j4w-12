package edu.geekhub.orcostat.beans;

import edu.geekhub.orcostat.controller.StatisticTableController;

public enum StatisticTableControllerBean {
    INSTANCE;

    private final StatisticTableController singleton = new StatisticTableController(
            StatisticTableServiceBean.INSTANCE.getSingleton()
    );

    public StatisticTableController getSingleton() {
        return singleton;
    }

    public StatisticTableController getPrototype() {
        return new StatisticTableController(StatisticTableServiceBean.INSTANCE.getPrototype());
    }
}

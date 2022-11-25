package edu.geekhub.orcostat.model.entity;

import java.time.LocalDate;

public class Drone extends MilitaryLoss {
    private static final int TWENTY_LADA_VESTA_SPORT_PRICE = 200_000;


    public Drone() {
        this(TWENTY_LADA_VESTA_SPORT_PRICE);
    }

    public Drone(LocalDate date) {
        this(TWENTY_LADA_VESTA_SPORT_PRICE, date);
    }

    public Drone(int price) {
        super(price);
    }

    public Drone(int price, LocalDate date) {
        super(price, date);
    }


    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }
}

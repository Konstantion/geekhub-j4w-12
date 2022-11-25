package edu.geekhub.orcostat.model.entity;

import java.time.LocalDate;

public class Orc extends MilitaryLoss {
    private static final int LADA_VESTA_SPORT_PRICE = 10_000;

    public Orc() {
        this(LADA_VESTA_SPORT_PRICE);
    }

    public Orc(LocalDate date) {
        this(LADA_VESTA_SPORT_PRICE, date);
    }

    public Orc(int price) {
        super(price);
    }

    public Orc(int price, LocalDate date) {
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

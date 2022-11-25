package edu.geekhub.orcostat.model.entity;

import java.time.LocalDate;

public class Missile extends MilitaryLoss {
    private static final int THREE_THOUSAND_LADA_VESTA_SPORT_PRICE = 30_000_000;


    public Missile() {
        this(THREE_THOUSAND_LADA_VESTA_SPORT_PRICE);
    }

    public Missile(LocalDate date) {
        this(THREE_THOUSAND_LADA_VESTA_SPORT_PRICE, date);
    }

    public Missile(int price) {
        super(price);
    }

    public Missile(int price, LocalDate date) {
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

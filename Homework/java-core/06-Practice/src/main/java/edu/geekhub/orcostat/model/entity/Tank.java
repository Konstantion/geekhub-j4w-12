package edu.geekhub.orcostat.model.entity;

import edu.geekhub.orcostat.model.collections.TrivialOrcCollection;

import java.time.LocalDate;

import static java.util.Objects.isNull;

public class Tank extends MilitaryLoss {
    private final TrivialOrcCollection equipage;
    private static final int THREE_HUNDRED_LADA_VESTA_SPORT_PRICE = 3_000_000;

    public Tank(TrivialOrcCollection equipage) {
        this(equipage, THREE_HUNDRED_LADA_VESTA_SPORT_PRICE);
    }

    public Tank() {
        this(new TrivialOrcCollection(), THREE_HUNDRED_LADA_VESTA_SPORT_PRICE);
    }

    public Tank(int price) {
        this(new TrivialOrcCollection(), price);
    }

    public Tank(LocalDate date) {
        this(new TrivialOrcCollection(), THREE_HUNDRED_LADA_VESTA_SPORT_PRICE, date);
    }

    public Tank(int price, LocalDate date) {
        this(new TrivialOrcCollection(), price, date);
    }

    public Tank(TrivialOrcCollection equipage, int price) {
        super(price);
        validate(equipage);
        this.equipage = equipage;
    }

    public Tank(TrivialOrcCollection equipage, int price, LocalDate date) {
        super(price, date);
        validate(equipage);
        this.equipage = equipage;
    }

    public TrivialOrcCollection getEquipage() {
        return equipage;
    }

    private void validate(TrivialOrcCollection equipage) {
        if (isNull(equipage)) {
            throw new IllegalArgumentException("Equipage cannot be null");
        }
        if (equipage.count() > 6) {
            throw new IllegalArgumentException("Too many orcs");
        }
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

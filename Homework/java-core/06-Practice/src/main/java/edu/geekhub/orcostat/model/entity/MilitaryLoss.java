package edu.geekhub.orcostat.model.entity;

import java.time.LocalDate;

public abstract class MilitaryLoss {
    protected final int price;
    protected final LocalDate date;

    protected MilitaryLoss(int price, LocalDate data) {
        validate(price);

        this.price = price;
        this.date = data;
    }

    protected MilitaryLoss(int price) {
        this(price, LocalDate.now());
    }

    protected abstract LocalDate getDate();

    protected abstract int getPrice();

    private void validate(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative number");
        }
    }
}

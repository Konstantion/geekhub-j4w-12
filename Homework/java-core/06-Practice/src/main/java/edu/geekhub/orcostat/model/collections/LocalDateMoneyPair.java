package edu.geekhub.orcostat.model.collections;

import java.time.LocalDate;
import java.util.Objects;

public class LocalDateMoneyPair {
    private LocalDate date;
    private long money;

    public LocalDateMoneyPair(LocalDate date, long money) {
        this.date = date;
        this.money = money;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public void addMoney(long money) {
        this.money += money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalDateMoneyPair pair = (LocalDateMoneyPair) o;
        return money == pair.money && Objects.equals(date, pair.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, money);
    }
}

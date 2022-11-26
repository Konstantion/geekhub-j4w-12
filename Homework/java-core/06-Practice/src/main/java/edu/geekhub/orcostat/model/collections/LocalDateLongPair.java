package edu.geekhub.orcostat.model.collections;

import java.time.LocalDate;
import java.util.Objects;

public class LocalDateLongPair {
    private LocalDate date;
    private long value;

    public LocalDateLongPair(LocalDate date, long value) {
        this.date = date;
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void addValue(long value) {
        this.value += value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalDateLongPair pair = (LocalDateLongPair) o;
        return value == pair.value && Objects.equals(date, pair.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, value);
    }
}

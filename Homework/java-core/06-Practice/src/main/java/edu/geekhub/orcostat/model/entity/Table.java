package edu.geekhub.orcostat.model.entity;

public class Table {
    private final String raw;

    public Table(String table) {
        this.raw = table;
    }

    @Override
    public String toString() {
        return raw;
    }
}

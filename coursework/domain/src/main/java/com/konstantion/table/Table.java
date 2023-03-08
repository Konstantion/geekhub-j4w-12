package com.konstantion.table;

import com.konstantion.order.Order;
import com.konstantion.user.User;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

public class Table {
    private Long id;
    private UUID uuid;
    private String name;
    private Integer capacity;
    private TableType tableType;
    private Order order;
    private List<User> waiters;
    private Boolean active;

    public Table() {
    }

    public boolean isOccupied() {
        return nonNull(order);
    }

    public static TableBuilder builder() {
        return new TableBuilder();
    }

    public static final class TableBuilder {
        private UUID uuid;
        private String name;
        private Integer capacity;
        private TableType tableType;
        private Order order;
        private List<User> waiters;
        private Boolean active;

        private TableBuilder() {
        }

        public TableBuilder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public TableBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TableBuilder capacity(Integer capacity) {
            this.capacity = capacity;
            return this;
        }

        public TableBuilder tableType(TableType tableType) {
            this.tableType = tableType;
            return this;
        }

        public TableBuilder order(Order order) {
            this.order = order;
            return this;
        }

        public TableBuilder waiters(List<User> waiters) {
            this.waiters = waiters;
            return this;
        }

        public TableBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Table build() {
            Table table = new Table();
            table.name = this.name;
            table.tableType = this.tableType;
            table.capacity = this.capacity;
            table.waiters = this.waiters;
            table.order = this.order;
            table.active = this.active;
            table.uuid = this.uuid;
            return table;
        }
    }
}

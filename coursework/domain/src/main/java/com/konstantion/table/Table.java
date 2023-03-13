package com.konstantion.table;

import com.konstantion.hall.Hall;
import com.konstantion.order.Order;
import com.konstantion.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Builder
@AllArgsConstructor
@Getter
@Setter

@Entity
@jakarta.persistence.Table(name = "table", schema = "public")
public class Table {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String name;
    private Integer capacity;
    private TableType tableType;

    @ManyToOne
    private Hall hall;
    @OneToOne
    private Order order;
    @OneToOne
    private User tableUser;

    @ManyToMany
    @JoinTable(
            name = "table_waiter",
            joinColumns = @JoinColumn(name = "table_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> waiters;
    private Boolean active;
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public Table() {
    }

    public boolean hasOrder() {
        return nonNull(order);
    }

    public boolean hasWaiters() {return waiters.isEmpty();}

    public boolean isActive() {return active;}
}

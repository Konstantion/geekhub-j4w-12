package com.konstantion.order;

import com.konstantion.product.Product;
import com.konstantion.table.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
@Builder
@AllArgsConstructor
@Getter
@Setter


@Entity
@jakarta.persistence.Table(name = "order", schema = "public")
public class Order {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Transient
    private Map<Product, Integer> products;

    @OneToOne
    @JoinColumn(
            name = "table_id",
            foreignKey = @ForeignKey(name = "order_table_fk")
    )
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Table table;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private Boolean active;

    public Order() {

    }
}

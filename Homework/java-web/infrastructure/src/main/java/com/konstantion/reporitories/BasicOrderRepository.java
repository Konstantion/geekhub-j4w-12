package com.konstantion.reporitories;

import com.konstantion.order.Order;
import com.konstantion.order.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class BasicOrderRepository implements OrderRepository {

    private Long id = 0L;
    private final List<Order> data;

    public BasicOrderRepository() {
        data = new ArrayList<>();
    }

    public Optional<Order> findById(Long id) {
        return data.stream()
                .filter(order -> order.id().equals(id))
                .findFirst();
    }

    public List<Order> findAll() {
        return data;
    }

    public Order saveAndFlush(Order order) {
        return save(order);
    }

    public Order save(Order order) {
        if (nonNull(order.id())) {
            return update(order);
        }

        Long id = nextId();

        order = order.setId(id);

        data.add(order);

        return order;
    }

    public void delete(Order order) {
        deleteById(order.id());
    }

    public void deleteById(Long id) {
        if (isNull(id)) {
            throw new IllegalArgumentException("Id shouldn't be null");
        }
        data.removeIf(dataOrder -> dataOrder.id().equals(id));
    }

    private Order update(Order order) {
        return order;
    }

    private Long nextId() {
        return ++id;
    }
}

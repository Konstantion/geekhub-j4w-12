package com.konstantion.order;


import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(Long id);

    List<Order> findAll();

    Order save(Order object);

    void delete(Order object);

    void deleteById(Long id);

    default Order saveAndFlush(Order object) {
        return save(object);
    }

    default List<Order> findAll(Sort sort) {
        throw new NotImplementedException();
    }
}

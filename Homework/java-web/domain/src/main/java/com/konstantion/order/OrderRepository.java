package com.konstantion.order;


import com.konstantion.utils.BasicCrudRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface OrderRepository extends BasicCrudRepository<Order, Long> {

    default List<Order> findAll(Sort sort) {
        throw new NotImplementedException();
    }
}

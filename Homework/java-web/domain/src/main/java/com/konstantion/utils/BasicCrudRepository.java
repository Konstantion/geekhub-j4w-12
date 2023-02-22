package com.konstantion.utils;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface BasicCrudRepository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();

    List<T> findAll(Sort sort);

    T save(T object);

    void delete(T object);

    void deleteById(ID id);

    default T saveAndFlush(T object) {
        return save(object);
    }
}

package com.konstantion.product;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Repository
public class ProductRepository {
    private Long id = 0L;
    private final List<Product> data;

    public ProductRepository() {
        data = new ArrayList<>();
    }

    public Optional<Product> findById(Long id) {
        return data.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    public List<Product> findAll() {
        return data;
    }

    public List<Product> findAll(Sort sort) {
        return data.stream()
                .sorted(getComparator(sort))
                .toList();
    }

    /**
     * Method to simplify future migration to JPA repository
     */
    public Product saveAndFlush(Product product) {
        return save(product);
    }

    public Product save(Product product) {
        if (nonNull(product.getId())) {
            return update(product);
        }

        Long id = nextId();

        product.setId(id);

        data.add(product);

        return product;
    }

    public void delete(Product product) {
        deleteById(product.getId());
    }

    public void deleteById(Long id) {
        if (isNull(id)) {
            throw new IllegalArgumentException("Id shouldn't be null");
        }
        data.removeIf(dataProduct -> dataProduct.getId().equals(id));
    }

    private Product update(Product product) {
        return product;
    }

    private Long nextId() {
        return ++id;
    }

    private Comparator<Product> getComparator(Sort sort) {
        Comparator<Product> comparator;
        Sort.Order order = sort.iterator().next();

        comparator = switch (order.getProperty()) {
            case "name" -> Comparator.comparing(Product::getName);
            case "price" -> Comparator.comparing(Product::getPrice);
            default -> Comparator.comparing(Product::getId);
        };

        if (order.getDirection().equals(Sort.Direction.DESC)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}

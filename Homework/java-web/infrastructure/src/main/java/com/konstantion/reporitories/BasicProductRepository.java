package com.konstantion.reporitories;

import com.konstantion.product.Product;
import com.konstantion.product.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class BasicProductRepository implements ProductRepository {
    private Long id = 0L;
    private final List<Product> data;

    public BasicProductRepository() {
        data = new ArrayList<>();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return data.stream()
                .filter(product -> product.id().equals(id))
                .findFirst();
    }

    @Override
    public List<Product> findAll() {
        return data;
    }

    @Override
    public List<Product> findAll(Sort sort) {
        return data.stream()
                .sorted(getComparator(sort))
                .toList();
    }

    /**
     * Method to simplify future migration to JPA repository
     */
    @Override
    public Product saveAndFlush(Product product) {
        return save(product);
    }

    @Override
    public Product save(Product product) {
        if (nonNull(product.id())) {
            return update(product);
        }

        Long id = nextId();

        product.setId(id);

        data.add(product);

        return product;
    }

    @Override
    public void delete(Product product) {
        deleteById(product.id());
    }

    @Override
    public void deleteById(Long id) {
        if (isNull(id)) {
            throw new IllegalArgumentException("Id shouldn't be null");
        }
        data.removeIf(dataProduct -> dataProduct.id().equals(id));
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
            case "name" -> Comparator.comparing(Product::name);
            case "price" -> Comparator.comparing(Product::price);
            default -> Comparator.comparing(Product::id);
        };

        if (order.getDirection().equals(Sort.Direction.DESC)) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}

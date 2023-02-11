package product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

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
        data.removeIf(dataProduct -> dataProduct.getId().equals(id));
    }

    private Product update(Product product) {
        return product;
    }

    private Long nextId() {
        return ++id;
    }
}

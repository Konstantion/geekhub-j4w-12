package product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

public class ProductRepository {
    private Integer id = 0;
    private final List<Product> data;

    public ProductRepository() {
        data = new ArrayList<>();
    }

    public Optional<Product> findById(Integer id) {
        return data.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    public List<Product> findAll() {
        return data;
    }



    public Product save(Product product) {
        if (nonNull(product.getId())) {
            return update(product);
        }

        Integer id = nextId();

        product.setId(id);

        data.add(product);

        return product;
    }

    public void delete( Product product) {
        data.removeIf(dataProduct -> dataProduct.equals(product));
    }

    private Product update(Product product) {
        return product;
    }

    private Integer nextId() {
        return id++;
    }
}

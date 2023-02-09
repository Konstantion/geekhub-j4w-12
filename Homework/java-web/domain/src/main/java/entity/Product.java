package entity;

import java.util.Objects;

public class Product {
    private String name;
    private Integer price;

    public Product() {
    }

    public Product(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(name, product.name) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        return "Product{" +
               "name='" + name + '\'' +
               ", price=" + price +
               '}';
    }

    public static final class ProductBuilder {
        private String name;
        private Integer price;

        private ProductBuilder() {
        }

        public static ProductBuilder builder() {
            return new ProductBuilder();
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder price(Integer price) {
            this.price = price;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            return product;
        }
    }
}

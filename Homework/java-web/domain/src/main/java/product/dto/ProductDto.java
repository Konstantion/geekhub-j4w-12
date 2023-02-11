package product.dto;

import java.time.LocalDateTime;

public class ProductDto {
    private Long id;
    private String name;
    private Integer price;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", price=" + price +
               ", createdAt=" + createdAt +
               '}';
    }
}

package product;

import com.konstantion.product.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ProductTest {
    @Test
    void someTest() {
        Product pr = mock(Product.class);
        assertThat(pr).isNotNull();
    }
}
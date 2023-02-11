package product;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import product.dto.ProductDto;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product entity);
}

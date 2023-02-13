package product;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import product.dto.CreationProductDto;
import product.dto.ProductDto;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product entity);

    List<ProductDto> toDto(List<Product> entity);

    Product toEntity(CreationProductDto dto);
}

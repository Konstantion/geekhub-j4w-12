package com.konstantion.dto.mappers;

import com.konstantion.dto.product.CreationProductDto;
import com.konstantion.dto.product.ProductDto;
import com.konstantion.dto.product.UpdateProductDto;
import com.konstantion.product.Product;
import com.konstantion.product.model.UpdateProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.konstantion.product.model.CreationProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static java.util.Objects.isNull;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product entity);

    List<ProductDto> toDto(List<Product> entity);

    Product toEntity(CreationProductRequest dto);

    Product toEntity(ProductDto dto);

    default Page<ProductDto> toDto(Page<Product> entity) {
        if(isNull(entity)) {
            return null;
        }
        List<ProductDto> dtos = entity.stream().map(this::toDto).toList();
        return new PageImpl<>(dtos, entity.getPageable(), entity.getTotalElements());
    }

    UpdateProductRequest toEntity(UpdateProductDto dto);

    CreationProductRequest toEntity(CreationProductDto dto);
}

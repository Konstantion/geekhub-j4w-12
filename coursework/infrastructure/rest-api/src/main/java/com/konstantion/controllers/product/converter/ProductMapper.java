package com.konstantion.controllers.product.converter;

import com.konstantion.controllers.product.dto.CreateProductRequestDto;
import com.konstantion.controllers.product.dto.ProductDto;
import com.konstantion.product.Product;
import com.konstantion.product.model.CreateProductRequest;
import com.konstantion.product.model.GetProductsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product entity);

    List<ProductDto> toDto(List<Product> entities);

    CreateProductRequest toCreateProductRequest(CreateProductRequestDto productRequestDto);

    default GetProductsRequest toGetProductsRequest(
            int pageNumber,
            int pageSize,
            String fieldName,
            String searchPattern,
            UUID categoryUuid,
            boolean ascending
    ) {
        return new GetProductsRequest(
                pageNumber,
                pageSize,
                fieldName,
                searchPattern,
                categoryUuid,
                ascending
        );
    }

    default Page<ProductDto> toDto(Page<Product> entities) {
        if(isNull(entities)) {
            return null;
        }
        List<ProductDto> dtos = entities.stream().map(this::toDto).toList();
        return new PageImpl<>(dtos, entities.getPageable(), entities.getTotalElements());
    }

}

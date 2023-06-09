package com.konstantion.dto.order.converter;

import com.konstantion.dto.order.dto.OrderProductsRequestDto;
import com.konstantion.order.Order;
import com.konstantion.dto.order.dto.OrderDto;
import com.konstantion.order.model.OrderProductsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDto toDto(Order order);

    List<OrderDto> toDto(List<Order> order);

    OrderProductsRequest toOrderProductsRequest(OrderProductsRequestDto dto);
}

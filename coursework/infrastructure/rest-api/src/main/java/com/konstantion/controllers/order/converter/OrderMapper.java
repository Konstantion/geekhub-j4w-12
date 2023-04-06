package com.konstantion.controllers.order.converter;

import com.konstantion.order.Order;
import com.konstantion.controllers.order.dto.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDto toDto(Order order);
}

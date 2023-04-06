package com.konstantion.controllers.hall.converter;

import com.konstantion.controllers.hall.dto.HallDto;
import com.konstantion.hall.Hall;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HallMapper {
    HallMapper INSTANCE = Mappers.getMapper(HallMapper.class);
    HallDto toDto(Hall hall);
}

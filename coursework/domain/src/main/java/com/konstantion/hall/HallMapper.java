package com.konstantion.hall;

import com.konstantion.hall.dto.HallDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HallMapper {
    static HallMapper INSTANCE = Mappers.getMapper(HallMapper.class);
    HallDto toDto(Hall hall);
}

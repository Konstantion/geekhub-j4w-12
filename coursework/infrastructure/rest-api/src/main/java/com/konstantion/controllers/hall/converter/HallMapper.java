package com.konstantion.controllers.hall.converter;

import com.konstantion.controllers.hall.dto.CreateHallRequestDto;
import com.konstantion.controllers.hall.dto.HallDto;
import com.konstantion.hall.Hall;
import com.konstantion.hall.model.CreateHallRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface HallMapper {
    HallMapper INSTANCE = Mappers.getMapper(HallMapper.class);

    HallDto toDto(Hall hall);

    List<HallDto> toDto(List<Hall> entities);

    CreateHallRequest toCreateHallRequest(CreateHallRequestDto createHallRequestDto);
}

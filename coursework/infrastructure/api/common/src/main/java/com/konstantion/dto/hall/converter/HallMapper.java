package com.konstantion.dto.hall.converter;

import com.konstantion.dto.hall.dto.CreateHallRequestDto;
import com.konstantion.dto.hall.dto.HallDto;
import com.konstantion.dto.hall.dto.UpdateHallRequestDto;
import com.konstantion.hall.Hall;
import com.konstantion.hall.model.CreateHallRequest;
import com.konstantion.hall.model.UpdateHallRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface HallMapper {
    HallMapper INSTANCE = Mappers.getMapper(HallMapper.class);

    HallDto toDto(Hall hall);

    List<HallDto> toDto(List<Hall> entities);

    CreateHallRequest toCreateHallRequest(CreateHallRequestDto createHallRequestDto);

    UpdateHallRequest toUpdateHallRequest(UpdateHallRequestDto updateHallRequestDto);
}

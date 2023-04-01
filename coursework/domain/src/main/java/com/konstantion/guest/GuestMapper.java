package com.konstantion.guest;

import com.konstantion.guest.dto.GuestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GuestMapper {
    GuestMapper INSTANCE = Mappers.getMapper(GuestMapper.class);

    GuestDto toDto(Guest entity);
}

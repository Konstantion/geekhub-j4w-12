package com.konstantion.controllers.guest.converter;

import com.konstantion.controllers.call.dto.CreateCallRequestDto;
import com.konstantion.guest.Guest;
import com.konstantion.controllers.guest.dto.GuestDto;
import com.konstantion.guest.model.CreateGuestRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface GuestMapper {
    GuestMapper INSTANCE = Mappers.getMapper(GuestMapper.class);

    GuestDto toDto(Guest entity);
    CreateGuestRequest toCreateGuestRequest(CreateCallRequestDto createCallRequestDto);

    List<GuestDto> toDto(List<Guest> entities);
}

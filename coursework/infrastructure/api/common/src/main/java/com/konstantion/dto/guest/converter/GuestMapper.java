package com.konstantion.dto.guest.converter;

import com.konstantion.dto.guest.dto.CreateGuestRequestDto;
import com.konstantion.dto.guest.dto.GuestDto;
import com.konstantion.dto.guest.dto.UpdateGuestRequestDto;
import com.konstantion.guest.Guest;
import com.konstantion.guest.model.CreateGuestRequest;
import com.konstantion.guest.model.UpdateGuestRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface GuestMapper {
    GuestMapper INSTANCE = Mappers.getMapper(GuestMapper.class);

    GuestDto toDto(Guest entity);

    CreateGuestRequest toCreateGuestRequest(CreateGuestRequestDto createCallRequestDto);

    List<GuestDto> toDto(List<Guest> entities);

    UpdateGuestRequest toUpdateGuestRequest(UpdateGuestRequestDto dto);
}

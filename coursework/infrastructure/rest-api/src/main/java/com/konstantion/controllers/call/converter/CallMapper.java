package com.konstantion.controllers.call.converter;

import com.konstantion.call.Call;
import com.konstantion.controllers.call.dto.CallDto;
import com.konstantion.controllers.call.dto.CreateCallRequestDto;
import com.konstantion.call.model.CreateCallRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CallMapper {
    CallMapper INSTANCE = Mappers.getMapper(CallMapper.class);

    CallDto toDto(Call call);

    CreateCallRequest toEntity(CreateCallRequestDto dto);
}

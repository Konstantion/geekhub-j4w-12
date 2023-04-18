package com.konstantion.dto.call.converter;

import com.konstantion.call.Call;
import com.konstantion.dto.call.dto.CallDto;
import com.konstantion.dto.call.dto.CreateCallRequestDto;
import com.konstantion.call.model.CreateCallRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CallMapper {
    CallMapper INSTANCE = Mappers.getMapper(CallMapper.class);

    CallDto toDto(Call call);

    CreateCallRequest toCreateCallRequest(CreateCallRequestDto dto);

    List<CallDto> toDto(List<Call> entities);
}

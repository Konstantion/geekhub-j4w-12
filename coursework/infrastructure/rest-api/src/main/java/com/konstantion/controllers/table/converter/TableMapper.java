package com.konstantion.controllers.table.converter;

import com.konstantion.controllers.table.dto.LoginTableRequestDto;
import com.konstantion.controllers.table.dto.TableDto;
import com.konstantion.table.Table;
import com.konstantion.table.model.LoginTableRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TableMapper {
    TableMapper INSTANCE = Mappers.getMapper(TableMapper.class);

    TableDto toDto(Table table);

    List<TableDto> toDto(List<Table> entities);

    LoginTableRequest toLoginTableRequest(LoginTableRequestDto requestDto);
}

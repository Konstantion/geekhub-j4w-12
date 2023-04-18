package com.konstantion.dto.table.converter;

import com.konstantion.dto.table.dto.*;
import com.konstantion.table.Table;
import com.konstantion.table.model.CreateTableRequest;
import com.konstantion.table.model.LoginTableRequest;
import com.konstantion.table.model.TableWaitersRequest;
import com.konstantion.table.model.UpdateTableRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TableMapper {
    TableMapper INSTANCE = Mappers.getMapper(TableMapper.class);

    TableDto toDto(Table table);

    List<TableDto> toDto(List<Table> entities);

    LoginTableRequest toLoginTableRequest(LoginTableRequestDto requestDto);

    CreateTableRequest toCreateTableRequest(CreateTableRequestDto requestDto);

    UpdateTableRequest toUpdateTableRequest(UpdateTableRequestDto requestDto);

    TableWaitersRequest toTableWaitersRequest(TableWaitersRequestDto requestDto);
}

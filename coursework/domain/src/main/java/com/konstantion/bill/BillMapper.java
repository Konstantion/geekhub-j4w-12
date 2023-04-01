package com.konstantion.bill;

import com.konstantion.bill.dto.BillDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BillMapper {
    BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

    BillDto toDto(Bill bill);
}

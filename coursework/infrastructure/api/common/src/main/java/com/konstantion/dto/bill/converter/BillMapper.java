package com.konstantion.dto.bill.converter;

import com.konstantion.bill.Bill;
import com.konstantion.bill.model.CreateBillRequest;
import com.konstantion.dto.bill.dto.BillDto;
import com.konstantion.dto.bill.dto.CreateBillRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BillMapper {
    BillMapper INSTANCE = Mappers.getMapper(BillMapper.class);

    BillDto toDto(Bill bill);

    List<BillDto> toDto(List<Bill> bill);

    CreateBillRequest toCreateBillRequest(CreateBillRequestDto dto);
}


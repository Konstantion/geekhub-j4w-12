package com.konstantion.table.dto;

import com.konstantion.table.TableType;

public record CreationTableDto(String name, Integer capacity, TableType tableType) {
}

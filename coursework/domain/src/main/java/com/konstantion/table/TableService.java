package com.konstantion.table;

import com.konstantion.table.dto.CreationTableDto;
import com.konstantion.table.dto.TableDto;
import com.konstantion.user.User;

public interface TableService {
    TableDto createTable(CreationTableDto creationTableDto, User user);
}

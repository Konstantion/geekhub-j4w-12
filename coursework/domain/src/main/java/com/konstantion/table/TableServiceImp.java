package com.konstantion.table;

import com.konstantion.table.dto.CreationTableDto;
import com.konstantion.table.dto.TableDto;
import com.konstantion.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import static com.konstantion.user.Permission.CREATE_TABLE;
import static com.konstantion.user.Role.ADMIN;

public record TableServiceImp() implements TableService {
    private static final Logger logger = LoggerFactory.getLogger(TableServiceImp.class);

    @Override
    public TableDto createTable(CreationTableDto creationTableDto, User user) {
        if(!user.hasPermission(ADMIN, CREATE_TABLE)) {
            //throw new FontFormatException("You don't have enough authorities to create table ");
        }
        return null;
    }
}

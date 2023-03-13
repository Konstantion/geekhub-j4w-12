package com.konstantion.table.validator;

import com.konstantion.table.Table;
import com.konstantion.table.TableType;
import com.konstantion.table.dto.CreationTableDto;
import com.konstantion.utils.validator.ValidationUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public record TableValidations() implements ValidationUtil {
    private static final String NAME_FIELD = "name";
    private static final String TABLE_TYPE_FIELD = "tableType";

    public Optional<FieldError> isTableNameValid(String name, Object table) {
        Set<String> violations = new HashSet<>();

        if (isBlank(name)) {
            violations.add("Name shouldn't be empty");
            return setToOptional(violations, table, NAME_FIELD);
        }

        return setToOptional(violations, table, NAME_FIELD);
    }

    public Optional<FieldError> isTableTypeValid(String tableType, CreationTableDto table) {
        Set<String> violations = new HashSet<>();

        if (isBlank(tableType)) {
            violations.add("Table type shouldn't be empty");
            return setToOptional(violations, table, NAME_FIELD);
        }

        try {
            TableType.valueOf(tableType);
        } catch (IllegalArgumentException e) {
            violations.add(format("No table type %s exist", tableType));
        }

        return setToOptional(violations, table, TABLE_TYPE_FIELD);
    }
}

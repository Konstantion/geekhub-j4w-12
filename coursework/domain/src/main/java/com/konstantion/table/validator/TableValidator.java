package com.konstantion.table.validator;

import com.konstantion.table.Table;
import com.konstantion.table.model.CreateTableRequest;
import com.konstantion.table.model.LoginTableRequest;
import com.konstantion.utils.validator.ValidationResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.HashSet;
import java.util.Set;

@Component
public record TableValidator(TableValidations validations) {
    public ValidationResult validate(Table table) {
        Set<FieldError> errors = new HashSet<>();

        validations.isTableNameValid(table.getName(), table).ifPresent(
                (errors::add)
        );

        return ValidationResult.of(errors);
    }

    public ValidationResult validate(CreateTableRequest table) {
        Set<FieldError> errors = new HashSet<>();

        validations.isTableNameValid(table.name(), table).ifPresent(
                (errors::add)
        );

        validations.isTableTypeValid(table.tableType(), table).ifPresent(
                (errors::add)
        );

        return ValidationResult.of(errors);
    }

    public ValidationResult validate(LoginTableRequest request) {
        Set<FieldError> errors = new HashSet<>();

        return ValidationResult.of(errors);
    }
}

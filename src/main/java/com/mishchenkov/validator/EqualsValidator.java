package com.mishchenkov.validator;

import com.mishchenkov.entity.DataContainer;

import java.util.Map;

public class EqualsValidator extends AbstractValidator {

    private final String fieldName;

    public EqualsValidator(DataContainer<String, String> dataPair, String fieldName, String errorMsg) {
        super(dataPair, errorMsg);
        this.fieldName = fieldName;
    }

    @Override
    public void validate(Map<String, String> errorMap) {
        if (dataPair.getName() == null || !dataPair.getName().equals(dataPair.getValue())) {
            errorMap.put(fieldName, errorMsg);
        } else {
            chain(errorMap);
        }
    }
}

package com.mishchenkov.validator;

import com.mishchenkov.entity.DataContainer;

import java.util.Map;

public class NullValidator extends AbstractValidator {

    public NullValidator(DataContainer<String, String> dataPair, String errorMsg) {
        super(dataPair, errorMsg);
    }

    @Override
    public void validate(Map<String, String> errorMap) {
        if (dataPair.getValue() == null) {
            errorMap.put(dataPair.getName(), errorMsg);
        } else {
            chain(errorMap);
        }
    }
}

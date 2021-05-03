package com.mishchenkov.validator;

import com.mishchenkov.entity.DataContainer;

import java.util.Map;

public class RegexpValidator extends AbstractValidator {

    private final String regexp;

    public RegexpValidator(DataContainer<String, String> dataPair, String errorMsg, String regexp) {
        super(dataPair, errorMsg);
        this.regexp = regexp;
    }

    @Override
    public void validate(Map<String, String> errorMap) {
        if (!dataPair.getValue().matches(regexp)) {
            errorMap.put(dataPair.getName(), errorMsg);
        } else {
            chain(errorMap);
        }
    }
}

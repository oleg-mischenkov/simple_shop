package com.mishchenkov.validator;

import com.mishchenkov.entity.DataContainer;

import java.util.Map;

public abstract class AbstractValidator implements Validator {

    protected final DataContainer<String, String> dataPair;
    protected final String errorMsg;
    private Validator next;

    protected AbstractValidator(DataContainer<String, String> dataPair, String errorMsg) {
        this.dataPair = dataPair;
        this.errorMsg = errorMsg;
    }

    @Override
    public void setNext(Validator next) {
        if (this.next == null) {
            this.next = next;
        } else {
            this.next.setNext(next);
        }
    }

    @Override
    public void chain(Map<String, String> errorMap) {
        if (next != null) {
            next.validate(errorMap);
        }
    }
}

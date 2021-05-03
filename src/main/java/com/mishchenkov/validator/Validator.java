package com.mishchenkov.validator;

import java.util.Map;

public interface Validator {

    void setNext(Validator next);
    void validate(Map<String, String> errorMap);
    void chain(Map<String, String> errorMap);

}

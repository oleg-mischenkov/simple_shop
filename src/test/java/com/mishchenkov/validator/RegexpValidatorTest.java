package com.mishchenkov.validator;

import com.mishchenkov.entity.DataContainer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

public class RegexpValidatorTest {

    private static final String REGEXP = "^([a-z]+)$";
    private static final String NAME = "name";
    private static final String ERROR_MSG = "Something went wrong";

    @Test
    public void shouldValueTestWhenThisOneIsIncorrect_validateTest() {
        //given
        Map<String, String> errorMap = new TreeMap<>();
        String value = "Test347";
        DataContainer<String, String> userName = new DataContainer<>(NAME, value);
        Validator validator = new RegexpValidator(userName, ERROR_MSG, REGEXP);

        //when
        validator.validate(errorMap);

        //then
        int mapSize = 1;
        Assert.assertEquals(1, mapSize);
        Assert.assertEquals(ERROR_MSG, errorMap.get(NAME));
    }

    @Test
    public void shouldValueTestWhenThisOneIsCorrect_validateTest() {
        //given
        Map<String, String> errorMap = new TreeMap<>();
        String value = "Test";
        DataContainer<String, String> userName = new DataContainer<>(NAME, value);
        Validator validator = new RegexpValidator(userName, ERROR_MSG, REGEXP);

        //when
        validator.validate(errorMap);

        //then
        int mapSize = 0;
        Assert.assertEquals(0, mapSize);
    }
}
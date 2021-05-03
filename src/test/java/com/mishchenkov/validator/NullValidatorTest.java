package com.mishchenkov.validator;

import com.mishchenkov.entity.DataContainer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

public class NullValidatorTest {

    @Test
    public void shouldValueTestWhenThisOneIsNull_validateTest() {
        //given
        Map<String, String> errorMap = new TreeMap<>();
        String name = "name";
        String msg = "Something went wrong";
        DataContainer<String, String> userName = new DataContainer<>(name, null);
        Validator validator = new NullValidator(userName, msg);

        //when
        validator.validate(errorMap);

        //then
        int mapSize = 1;
        Assert.assertEquals(1, mapSize);
        Assert.assertEquals(msg, errorMap.get(name));
    }

    @Test
    public void shouldValueTestWhenThisOneIsConsist_validateTest() {
        //given
        Map<String, String> errorMap = new TreeMap<>();
        String name = "name";
        String value = "Jhon";
        String msg = "Something went wrong";
        DataContainer<String, String> userName = new DataContainer<>(name, value);
        Validator validator = new NullValidator(userName, msg);

        //when
        validator.validate(errorMap);

        //then
        int mapSize = 0;
        Assert.assertEquals(0, mapSize);
    }
}
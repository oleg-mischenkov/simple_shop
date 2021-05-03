package com.mishchenkov.xml;

public interface XMLParser<T> {

    T parse(String xmlPath);

}

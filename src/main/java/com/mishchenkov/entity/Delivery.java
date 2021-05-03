package com.mishchenkov.entity;

import java.io.Serializable;
import java.util.Objects;

public class Delivery implements Serializable {

    private static final long serialVersionUID = -43874501881480473L;

    private final String name;

    public Delivery(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(name, delivery.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "name='" + name + '\'' +
                '}';
    }
}

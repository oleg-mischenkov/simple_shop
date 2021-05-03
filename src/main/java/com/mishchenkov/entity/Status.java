package com.mishchenkov.entity;

import java.io.Serializable;
import java.util.Objects;

public class Status implements Serializable {

    private static final long serialVersionUID = 1592647738849267120L;

    private final String name;

    public Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status that = (Status) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "name='" + name + '\'' +
                '}';
    }
}

package com.mishchenkov.entity;

import java.io.Serializable;
import java.util.Objects;

public class Manufacture implements Serializable {

    private static final long serialVersionUID = 2418973898059351263L;

    private String name;

    public Manufacture() {}

    public Manufacture(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manufacture that = (Manufacture) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Manufacture{" +
                "name='" + name + '\'' +
                '}';
    }
}

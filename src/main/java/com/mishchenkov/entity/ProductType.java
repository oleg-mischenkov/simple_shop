package com.mishchenkov.entity;

import java.io.Serializable;
import java.util.Objects;

public class ProductType implements Serializable {

    private static final long serialVersionUID = -9189874289892552652L;

    private String name;

    public ProductType() {}

    public ProductType(String name) {
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
        ProductType that = (ProductType) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ProductType{" +
                "name='" + name + '\'' +
                '}';
    }
}

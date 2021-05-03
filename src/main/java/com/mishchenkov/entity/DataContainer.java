package com.mishchenkov.entity;

import java.io.Serializable;
import java.util.Objects;

public class DataContainer<N extends Serializable, V extends Serializable> implements Serializable {

    private static final long serialVersionUID = -7461446338980919013L;

    private N name;
    private V value;

    public DataContainer() {
    }

    public DataContainer(N name, V value) {
        this.name = name;
        this.value = value;
    }

    public N getName() {
        return name;
    }

    public void setName(N name) {
        this.name = name;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public DataContainer<V, N> changeData() {
        return new DataContainer<>(getValue(), getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataContainer<?, ?> that = (DataContainer<?, ?>) o;
        return Objects.equals(name, that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return "DataContainer{" +
                "name=" + name +
                ", value=" + value +
                '}';
    }
}

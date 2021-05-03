package com.mishchenkov.dto;

import com.mishchenkov.entity.Manufacture;

import java.io.Serializable;
import java.util.Objects;

public class ManufactureDTO implements Serializable {

    private static final long serialVersionUID = -1415898856922249549L;

    private long key;
    private Manufacture manufacture;

    public long getKey() {
        return key;
    }

    public ManufactureDTO setKey(long key) {
        this.key = key;
        return this;
    }

    public Manufacture getManufacture() {
        return manufacture;
    }

    public ManufactureDTO setManufacture(Manufacture manufacture) {
        this.manufacture = manufacture;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManufactureDTO that = (ManufactureDTO) o;
        return key == that.key && manufacture.equals(that.manufacture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, manufacture);
    }

    @Override
    public String toString() {
        return "ManufactureDTO{" +
                "key=" + key +
                ", manufacture=" + manufacture +
                '}';
    }
}

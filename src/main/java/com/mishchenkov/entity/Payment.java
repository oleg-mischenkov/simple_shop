package com.mishchenkov.entity;

import java.io.Serializable;
import java.util.Objects;

public class Payment implements Serializable {

    private static final long serialVersionUID = 4388529510668470542L;

    private final String name;

    public Payment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(name, payment.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "name='" + name + '\'' +
                '}';
    }
}

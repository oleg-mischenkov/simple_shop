package com.mishchenkov.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * This is immutable class witch consists the money.
 */
public final class Money implements Serializable {

    private static final long serialVersionUID = -9150923152089165737L;

    private final int moneyValue;

    /**
     * Constructor is initialising the money value as 0.
     */
    public Money() {
        moneyValue = 0;
    }

    /**
     * Constructor is initialising the money value as value as the money argument.
     *
     * @param money - value in cents. For example if money value is 200 then it will be equal as $2.0.
     */
    public Money(int money) {
        this.moneyValue = money;
    }

    /**
     * If money value is $2 it will return 200.
     *
     * @return - money in cents.
     */
    public int getMoneyInCents() {
        return moneyValue;
    }

    /**
     * If money value is $2 it will return 2.0
     *
     * @return - the money with a float point.
     */
    public float getMoneyAsFloat() {
        return (float) moneyValue / 100;
    }

    /**
     * * If the money value is $2.5, the result is 3.
     * * If the money value is $2.4, the result is 2.
     *
     * @return - value without some tail after a point
     */
    public int getMoneyValue() {
        return Math.round((float) moneyValue / 100);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money1 = (Money) o;
        return moneyValue == money1.moneyValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(moneyValue);
    }

    @Override
    public String toString() {
        return String.format("%.2f", getMoneyAsFloat());
    }
}

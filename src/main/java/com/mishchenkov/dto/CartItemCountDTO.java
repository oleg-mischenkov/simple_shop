package com.mishchenkov.dto;

import java.io.Serializable;
import java.util.Objects;

public class CartItemCountDTO implements Serializable {

    private static final long serialVersionUID = 380253628742854405L;

    private String productSku;
    private String value;

    public String getProductSku() {
        return productSku;
    }

    public CartItemCountDTO setProductSku(String productSku) {
        this.productSku = productSku;
        return this;
    }

    public String getValue() {
        return value;
    }

    public CartItemCountDTO setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemCountDTO that = (CartItemCountDTO) o;
        return Objects.equals(productSku, that.productSku) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productSku, value);
    }

    @Override
    public String toString() {
        return "CartItemCountDTO{" +
                "productSku='" + productSku + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

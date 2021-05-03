package com.mishchenkov.dto;

import com.mishchenkov.entity.ProductType;

import java.io.Serializable;
import java.util.Objects;

public class ProductTypeDTO implements Serializable {

    private static final long serialVersionUID = 1952076023900232273L;

    private long key;
    private ProductType productType;

    public long getKey() {
        return key;
    }

    public ProductTypeDTO setKey(long key) {
        this.key = key;
        return this;
    }

    public ProductType getProductType() {
        return productType;
    }

    public ProductTypeDTO setProductType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductTypeDTO that = (ProductTypeDTO) o;
        return key == that.key && productType.equals(that.productType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, productType);
    }

    @Override
    public String toString() {
        return "ProductTypeDTO{" +
                "key=" + key +
                ", productType=" + productType +
                '}';
    }
}
